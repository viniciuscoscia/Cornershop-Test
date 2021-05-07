package com.cornershop.counterstest.presentation.ui.main

import androidx.lifecycle.*
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.entities.Counter
import com.cornershop.counterstest.domain.usecases.DecreaseCounterUseCase
import com.cornershop.counterstest.domain.usecases.DeleteCounterUseCase
import com.cornershop.counterstest.domain.usecases.GetCountersUseCase
import com.cornershop.counterstest.domain.usecases.IncreaseCounterUseCase
import com.cornershop.counterstest.presentation.commons.BaseViewModel
import com.cornershop.counterstest.presentation.commons.ViewState
import com.cornershop.counterstest.presentation.commons.errorevent.CommonErrorEvents
import com.cornershop.counterstest.presentation.commons.errorevent.CountersErrorEvents
import com.cornershop.counterstest.presentation.commons.errorevent.ErrorEvent
import com.cornershop.counterstest.presentation.mapper.toUIModel
import com.cornershop.counterstest.presentation.model.CounterUiModel
import com.cornershop.counterstest.presentation.model.CountersFragmentUiModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val getCountersUseCase: GetCountersUseCase,
    private val increaseCounterUseCase: IncreaseCounterUseCase,
    private val decreaseCounterUseCase: DecreaseCounterUseCase,
    private val deleteCounterUseCase: DeleteCounterUseCase
) : BaseViewModel() {
    private val _countersLiveData: MutableLiveData<ViewState<CountersFragmentUiModel>> =
        MutableLiveData()
    val countersLiveData: LiveData<ViewState<CountersFragmentUiModel>> = _countersLiveData

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getCounters() = viewModelScope.launch {
        _countersLiveData.postValue(ViewState.Loading)

        val viewState = when (val useCaseResult = getCountersUseCase()) {
            is ResultWrapper.Success -> {
                ViewState.Success(buildCountersUiModel(useCaseResult.value))
            }
            is ResultWrapper.NetworkErrorEntity.Timeout -> {
                ViewState.Error(CountersErrorEvents.GetCountersNetworkUnavailable)
            }
            else -> {
                ViewState.Error(CommonErrorEvents.Generic)
            }
        }

        _countersLiveData.postValue(viewState)
    }

    fun onIncreaseCounter(counterUiModel: CounterUiModel) = viewModelScope.launch {
        _countersLiveData.postValue(ViewState.Loading)

        val viewState: ViewState<CountersFragmentUiModel> =
            when (val useCaseResult = increaseCounterUseCase(counterUiModel.id)) {
                is ResultWrapper.Success -> {
                    ViewState.Success(buildCountersUiModel(useCaseResult.value))
                }
                is ResultWrapper.NetworkErrorEntity.Timeout -> {
                    val errorEvent: ErrorEvent =
                        CountersErrorEvents.IncreaseCounterNetworkUnavailable(counterUiModel)
                    ViewState.Error(errorEvent)
                }
                else -> {
                    ViewState.Error(CommonErrorEvents.Generic)
                }
            }

        _countersLiveData.postValue(viewState)
    }

    fun onDecreaseCounter(counterUiModel: CounterUiModel) = viewModelScope.launch {
        _countersLiveData.postValue(ViewState.Loading)

        val viewState: ViewState<CountersFragmentUiModel> =
            when (val useCaseResult = decreaseCounterUseCase(counterUiModel.id)) {
                is ResultWrapper.Success -> {
                    ViewState.Success(buildCountersUiModel(useCaseResult.value))
                }
                is ResultWrapper.NetworkErrorEntity.Timeout -> {
                    val errorEvent: ErrorEvent =
                        CountersErrorEvents.DecreaseCounterNetworkUnavailable(counterUiModel)
                    ViewState.Error(errorEvent)
                }
                else -> {
                    ViewState.Error(CommonErrorEvents.Generic)
                }
            }

        _countersLiveData.postValue(viewState)
    }

    fun onDeleteCounter(counterId: String) = viewModelScope.launch {
        _countersLiveData.postValue(ViewState.Loading)

        val viewState: ViewState<CountersFragmentUiModel> =
            when (val useCaseResult = deleteCounterUseCase(counterId)) {
                is ResultWrapper.Success -> {
                    ViewState.Success(buildCountersUiModel(useCaseResult.value))
                }
                is ResultWrapper.NetworkErrorEntity.Timeout -> {
                    ViewState.Error(CountersErrorEvents.DeleteCounterNetworkUnavailable)
                }
                else -> {
                    ViewState.Error(CommonErrorEvents.Generic)
                }
            }

        _countersLiveData.postValue(viewState)
    }

    private fun buildCountersUiModel(counters: List<Counter>) = CountersFragmentUiModel(
        itemsCount = counters.size,
        timesCount = counters.sumOf { counter -> counter.count },
        counters = counters.toUIModel()
    )
}