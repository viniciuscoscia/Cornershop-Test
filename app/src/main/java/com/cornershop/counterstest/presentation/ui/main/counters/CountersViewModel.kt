package com.cornershop.counterstest.presentation.ui.main.counters

import androidx.lifecycle.*
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.entities.Counter
import com.cornershop.counterstest.domain.usecases.counter.DecreaseCounterUseCase
import com.cornershop.counterstest.domain.usecases.counter.DeleteCounterUseCase
import com.cornershop.counterstest.domain.usecases.counter.GetCountersUseCase
import com.cornershop.counterstest.domain.usecases.counter.IncreaseCounterUseCase
import com.cornershop.counterstest.presentation.commons.BaseViewModel
import com.cornershop.counterstest.presentation.commons.errorevent.CommonErrorEvents
import com.cornershop.counterstest.presentation.commons.errorevent.CountersErrorEvents
import com.cornershop.counterstest.presentation.commons.errorevent.ErrorEvent
import com.cornershop.counterstest.presentation.commons.viewstate.MultiSelectionState
import com.cornershop.counterstest.presentation.commons.viewstate.ViewState
import com.cornershop.counterstest.presentation.mapper.toUIModel
import com.cornershop.counterstest.presentation.model.CounterUiModel
import com.cornershop.counterstest.presentation.model.CountersFragmentUiModel
import kotlinx.coroutines.launch

class CountersViewModel(
	private val getCountersUseCase: GetCountersUseCase,
	private val increaseCounterUseCase: IncreaseCounterUseCase,
	private val decreaseCounterUseCase: DecreaseCounterUseCase,
	private val deleteCounterUseCase: DeleteCounterUseCase
) : BaseViewModel() {
	private val _countersLiveData: MutableLiveData<ViewState<CountersFragmentUiModel>> =
		MutableLiveData()
	val countersLiveData: LiveData<ViewState<CountersFragmentUiModel>> = _countersLiveData

	private val _multiSelectionLiveData: MutableLiveData<MultiSelectionState> = MutableLiveData()
	val multiSelectionLiveData = _multiSelectionLiveData

	private var selectedCounterId: String? = null

	fun enterMultiSelectionMode(counterId: String) {
		selectedCounterId = counterId

		_multiSelectionLiveData.postValue(MultiSelectionState.Enabled)
	}

	fun exitMultiSelectionMode() {
		selectedCounterId = null

		_multiSelectionLiveData.postValue(MultiSelectionState.Disabled)
	}

	fun getCounters(isSwipeRefresh: Boolean = false) = viewModelScope.launch {
		_countersLiveData.postValue(
			if (isSwipeRefresh)
				ViewState.Loading.SwipeRefresh
			else
				ViewState.Loading.Normal
		)

		val viewState = when (val useCaseResult = getCountersUseCase()) {
			is ResultWrapper.Success -> {
				ViewState.Success(buildCountersUiModel(useCaseResult.value))
			}
			is ResultWrapper.NetworkErrorEntity.Timeout,
			is ResultWrapper.NetworkErrorEntity.NoInternetConnection -> {
				ViewState.Error(CountersErrorEvents.GetCountersNetworkUnavailable)
			}
			else -> {
				ViewState.Error(CommonErrorEvents.Generic)
			}
		}

		_countersLiveData.postValue(viewState)
	}

	fun onIncreaseCounter(counterUiModel: CounterUiModel) = viewModelScope.launch {
		_countersLiveData.postValue(ViewState.Loading.Normal)

		val viewState: ViewState<CountersFragmentUiModel> =
			when (val useCaseResult = increaseCounterUseCase(counterUiModel.id)) {
				is ResultWrapper.Success -> {
					ViewState.Success(buildCountersUiModel(useCaseResult.value))
				}
				is ResultWrapper.NetworkErrorEntity.NoInternetConnection -> {
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
		_countersLiveData.postValue(ViewState.Loading.Normal)

		val viewState: ViewState<CountersFragmentUiModel> =
			when (val useCaseResult = decreaseCounterUseCase(counterUiModel.id)) {
				is ResultWrapper.Success -> {
					ViewState.Success(buildCountersUiModel(useCaseResult.value))
				}
				is ResultWrapper.NetworkErrorEntity.NoInternetConnection -> {
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

	fun onDeleteCounter() = viewModelScope.launch {
		_countersLiveData.postValue(ViewState.Loading.Normal)

		if (selectedCounterId.isNullOrBlank()) {
			ViewState.Error(CommonErrorEvents.Generic)
			return@launch
		}

		val viewState: ViewState<CountersFragmentUiModel> =
			when (val useCaseResult = deleteCounterUseCase(counterId = selectedCounterId!!)) {
				is ResultWrapper.Success -> {
					ViewState.Success(buildCountersUiModel(useCaseResult.value))
				}
				is ResultWrapper.NetworkErrorEntity.NoInternetConnection -> {
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