package com.cornershop.counterstest.domain.di

import com.cornershop.counterstest.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {
    factory {
        GetCountersUseCase(repository = get())
    }
    factory {
        CreateCounterUseCase(repository = get())
    }
    factory {
        IncreaseCounterUseCase(repository = get())
    }
    factory {
        DecreaseCounterUseCase(repository = get())
    }
    factory {
        DeleteCounterUseCase(repository = get())
    }
    factory {
        ShareCounterUseCase()
    }
}