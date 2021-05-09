package com.cornershop.counterstest.presentation.ui.main.counterexamples

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cornershop.counterstest.domain.usecases.GetExamplesUseCase
import com.cornershop.counterstest.presentation.mapper.toUIModel

class CounterExamplesViewModel(
    private val getExamplesUseCase: GetExamplesUseCase
) : ViewModel(), LifecycleObserver {
    val examplesLiveData = liveData {
        emit(getExamplesUseCase().toUIModel())
    }
}