package com.cornershop.counterstest.presentation

import android.app.Application
import com.cornershop.counterstest.data.di.dataModules
import com.cornershop.counterstest.domain.di.domainModule
import com.cornershop.counterstest.presentation.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CounterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CounterApplication)
            modules(applicationModule + dataModules + domainModule)
        }
    }
}