package com.cornershop.counterstest.data.di

import com.cornershop.counterstest.BuildConfig
import com.cornershop.counterstest.data.remote.api.CounterAPI
import com.cornershop.counterstest.data.remote.datasource.CounterRemoteDataSource
import com.cornershop.counterstest.data.remote.datasource.CounterRemoteDataSourceImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val remoteDataSourceModule = module {
    factory { providesOkHttpClient() }

    single {
        createWebService<CounterAPI>(
            okHttpClient = get(),
            url = BuildConfig.COUNTER_BASE_URL
        )
    }

    factory<CounterRemoteDataSource> { CounterRemoteDataSourceImpl(counterAPI = get()) }
}

private const val DEFAULT_TIMEOUT_SECONDS = 30L

fun providesOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    return Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(url)
        .client(okHttpClient)
        .build()
        .create(T::class.java)
}
