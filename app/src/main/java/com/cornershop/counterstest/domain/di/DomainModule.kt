package com.cornershop.counterstest.domain.di

import com.cornershop.counterstest.domain.usecases.GetCountersUseCase
import org.koin.dsl.module

val domainModule = module {
    factory {
        GetCountersUseCase(repository = get())
    }
}