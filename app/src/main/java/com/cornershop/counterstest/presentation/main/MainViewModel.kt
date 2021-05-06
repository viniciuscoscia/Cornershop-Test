package com.cornershop.counterstest.presentation.main

import androidx.lifecycle.*
import com.cornershop.counterstest.data.helper.ErrorEntity
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.entities.Counter
import com.cornershop.counterstest.domain.usecases.GetCountersUseCase
import com.cornershop.counterstest.presentation.commons.BaseViewModel
import com.cornershop.counterstest.presentation.mapper.toUIModel
import com.cornershop.counterstest.presentation.model.CounterUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val countersUseCase: GetCountersUseCase
) : BaseViewModel() {
    private val _countersLiveData: MutableLiveData<List<CounterUiModel>> = MutableLiveData()
    val countersLiveData: LiveData<List<CounterUiModel>> = _countersLiveData

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getCounters() = viewModelScope.launch(Dispatchers.IO) {
        when (val result: ResultWrapper<List<Counter>> = countersUseCase()) {
            is ResultWrapper.Success -> _countersLiveData.postValue(result.value.toUIModel())
            is ResultWrapper.Error -> {
                when (result.errorEntity) {
                    is ErrorEntity.
                }
            }
        }
    }
}