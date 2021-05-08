package com.cornershop.counterstest.presentation.di

import com.cornershop.counterstest.presentation.ui.main.counters.CountersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module(override = true) {
    viewModel {
        CountersViewModel(
            getCountersUseCase = get(),
            increaseCounterUseCase = get(),
            decreaseCounterUseCase = get(),
            deleteCounterUseCase = get()
        )
    }
}