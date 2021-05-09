package com.cornershop.counterstest.data.di

import com.cornershop.counterstest.data.repository.CounterRepository
import com.cornershop.counterstest.data.repository.CounterRepositoryImpl
import com.cornershop.counterstest.data.repository.ExamplesRepository
import com.cornershop.counterstest.data.repository.ExamplesRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule = module {
    single<CounterRepository> {
        CounterRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        )
    }

    single<ExamplesRepository> {
        ExamplesRepositoryImpl(
            examplesLocalDataSource = get()
        )
    }
}

val dataModules: List<Module> = listOf(remoteDataSourceModule, repositoryModule, localDataModule)