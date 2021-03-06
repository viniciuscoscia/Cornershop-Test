package com.cornershop.counterstest.presentation.ui.main.createconter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.usecases.counter.CreateCounterUseCase
import com.cornershop.counterstest.presentation.commons.errorevent.CommonErrorEvents
import com.cornershop.counterstest.presentation.commons.errorevent.CountersErrorEvents
import com.cornershop.counterstest.presentation.commons.viewstate.ViewState
import kotlinx.coroutines.launch

class CreateCounterViewModel(
    private val createCounterUseCase: CreateCounterUseCase
) : ViewModel() {
    private val _counterLiveData = MutableLiveData<ViewState<Nothing>>()
    val counterLiveData = _counterLiveData

    fun createCounter(counterTitle: String) = viewModelScope.launch {
        _counterLiveData.postValue(ViewState.Loading.Normal)

        val viewState: ViewState<Nothing> =
            when (createCounterUseCase(counterTitle)) {
                is ResultWrapper.Success -> {
                    ViewState.SuccessEmpty
                }
                is ResultWrapper.NetworkErrorEntity.Timeout,
                is ResultWrapper.NetworkErrorEntity.NoInternetConnection -> {
                    ViewState.Error(CountersErrorEvents.CreateCounterErrorEvent)
                }
                else -> {
                    ViewState.Error(CommonErrorEvents.Generic)
                }
            }

        _counterLiveData.postValue(viewState)
    }
}