package com.cornershop.counterstest.presentation.main

import android.util.Log
import androidx.lifecycle.*
import com.cornershop.counterstest.domain.usecases.GetCountersUseCase
import com.cornershop.counterstest.presentation.commons.BaseViewModel
import com.cornershop.counterstest.presentation.model.CounterUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val countersUseCase: GetCountersUseCase
) : BaseViewModel() {

    private val _countersLiveData: MutableLiveData<List<CounterUiModel>> = MutableLiveData()
    val countersLiveData: LiveData<List<CounterUiModel>> = _countersLiveData

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun a() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Vinicius", countersUseCase().toString())
        }
    }
}