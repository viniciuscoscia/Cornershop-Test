package com.cornershop.counterstest.domain.di

import com.cornershop.counterstest.domain.usecases.GetExamplesUseCase
import com.cornershop.counterstest.domain.usecases.counter.*
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
		GetExamplesUseCase(repository = get())
	}
	factory {
		SearchCountersByTextUseCase(repository = get())
	}
}