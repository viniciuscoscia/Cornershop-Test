package com.cornershop.counterstest.presentation.ui.main.counterexamples

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.domain.usecases.GetExamplesUseCase

class CounterExamplesViewModel(
    private val getExamplesUseCase: GetExamplesUseCase
) : ViewModel(), LifecycleObserver {

}