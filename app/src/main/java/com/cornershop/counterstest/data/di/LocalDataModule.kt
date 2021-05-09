package com.cornershop.counterstest.data.di

import com.cornershop.counterstest.data.local.dao.CounterDAO
import com.cornershop.counterstest.data.local.database.CountersDatabase
import com.cornershop.counterstest.data.local.datasource.CountersLocalDataSource
import com.cornershop.counterstest.data.local.datasource.CountersLocalDataSourceImpl
import com.cornershop.counterstest.data.local.datasource.ExamplesLocalDataSource
import com.cornershop.counterstest.data.local.datasource.ExamplesLocalDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDataModule = module {
    single<ExamplesLocalDataSource> {
        ExamplesLocalDataSourceImpl(context = androidApplication())
    }

    single<CountersLocalDataSource> {
        CountersLocalDataSourceImpl(countersDao = get())
    }

    single<CounterDAO> {
        CountersDatabase.createDatabase(context = androidApplication())
    }
}