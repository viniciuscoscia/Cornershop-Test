package com.cornershop.counterstest.presentation.di

import com.cornershop.counterstest.presentation.ui.main.counterexamples.CounterExamplesViewModel
import com.cornershop.counterstest.presentation.ui.main.counters.CountersViewModel
import com.cornershop.counterstest.presentation.ui.main.createconter.CreateCounterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module(override = true) {
    viewModel {
        CountersViewModel(
	        getCountersUseCase = get(),
	        increaseCounterUseCase = get(),
	        decreaseCounterUseCase = get(),
	        deleteCounterUseCase = get(),
	        searchCountersByTextUseCase = get()
        )
    }

    viewModel {
        CreateCounterViewModel(
            createCounterUseCase = get()
        )
    }

    viewModel {
        CounterExamplesViewModel(getExamplesUseCase = get())
    }
}