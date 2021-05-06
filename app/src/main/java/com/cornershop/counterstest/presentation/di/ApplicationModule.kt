package com.cornershop.counterstest.presentation.di

import com.cornershop.counterstest.presentation.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module(override = true) {
    viewModel {
        MainViewModel(countersUseCase = get())
    }
}