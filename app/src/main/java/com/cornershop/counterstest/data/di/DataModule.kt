package com.cornershop.counterstest.data.di

import com.cornershop.counterstest.data.CounterRepository
import com.cornershop.counterstest.data.CounterRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule = module {
    factory<CounterRepository> {
        CounterRepositoryImpl(
            remoteDataSource = get()
        )
    }
}

val dataModules: List<Module> = listOf(remoteDataSourceModule, repositoryModule, localDataModule)