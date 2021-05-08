package com.cornershop.counterstest.presentation.ui.main.createconter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.usecases.CreateCounterUseCase
import com.cornershop.counterstest.presentation.commons.ViewState
import com.cornershop.counterstest.presentation.commons.errorevent.CountersErrorEvents
import kotlinx.coroutines.launch

class CreateCounterViewModel(
    private val createCounterUseCase: CreateCounterUseCase
) : ViewModel() {
    private val _counterLiveData = MutableLiveData<ViewState<Nothing>>()
    val counterLiveData = _counterLiveData

    fun createCounter(counterTitle: String) = viewModelScope.launch {
        _counterLiveData.postValue(ViewState.Loading)

        val viewState: ViewState<Nothing> =
            when (createCounterUseCase(counterTitle)) {
                is ResultWrapper.Success -> {
                    ViewState.SuccessEmpty
                }
                is ResultWrapper.NetworkErrorEntity -> {
                    ViewState.Error(CountersErrorEvents.CreateCounterErrorEvent)
                }
            }

        _counterLiveData.postValue(viewState)
    }
}