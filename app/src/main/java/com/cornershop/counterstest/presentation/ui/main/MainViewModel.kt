package com.cornershop.counterstest.presentation.ui.main

import androidx.lifecycle.*
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.usecases.GetCountersUseCase
import com.cornershop.counterstest.presentation.commons.BaseViewModel
import com.cornershop.counterstest.presentation.commons.ViewState
import com.cornershop.counterstest.presentation.commons.errorevent.CommonErrorEvents
import com.cornershop.counterstest.presentation.mapper.toUIModel
import com.cornershop.counterstest.presentation.model.CounterUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val countersUseCase: GetCountersUseCase
) : BaseViewModel() {
    private val _countersLiveData: MutableLiveData<ViewState<List<CounterUiModel>>> =
        MutableLiveData()
    val countersLiveData: LiveData<ViewState<List<CounterUiModel>>> = _countersLiveData

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getCounters() = viewModelScope.launch(Dispatchers.IO) {
        _countersLiveData.postValue(ViewState.Loading)

        val viewState = when (val useCaseResult = countersUseCase()) {
            is ResultWrapper.Success -> {
                ViewState.Success(useCaseResult.value.toUIModel())
            }
            is ResultWrapper.ErrorEntity.Network -> {
                ViewState.Error(CommonErrorEvents.NETWORK_UNAVAILABLE)
            }
            else -> {
                ViewState.Error(CommonErrorEvents.GENERIC)
            }
        }

        _countersLiveData.postValue(viewState)
    }
}