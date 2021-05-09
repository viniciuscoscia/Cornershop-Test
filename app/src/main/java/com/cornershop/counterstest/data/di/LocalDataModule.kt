package com.cornershop.counterstest.data.di

import com.cornershop.counterstest.data.local.datasource.ExamplesLocalDataSource
import com.cornershop.counterstest.data.local.datasource.ExamplesLocalDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDataModule = module {
    single<ExamplesLocalDataSource> {
        ExamplesLocalDataSourceImpl(context = androidApplication())
    }
}