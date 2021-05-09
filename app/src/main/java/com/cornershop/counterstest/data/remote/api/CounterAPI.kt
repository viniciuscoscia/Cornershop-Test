package com.cornershop.counterstest.data.remote.api

import com.cornershop.counterstest.data.remote.entity.AddCounterRequest
import com.cornershop.counterstest.data.remote.entity.CounterResponse
import com.cornershop.counterstest.data.remote.entity.GeneralCounterRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface CounterAPI {
    @GET("$BASE_ENDPOINT/counters")
    suspend fun getCounters(): List<CounterResponse>

    @POST("$BASE_ENDPOINT/counter")
    suspend fun postAddCounter(@Body title: AddCounterRequest): List<CounterResponse>

    @POST("$BASE_ENDPOINT/counter/inc")
    suspend fun postIncreaseCounter(@Body id: GeneralCounterRequest): List<CounterResponse>

    @POST("$BASE_ENDPOINT/counter/dec")
    suspend fun postDecreaseCounter(@Body id: GeneralCounterRequest): List<CounterResponse>

    @DELETE("$BASE_ENDPOINT/counter")
    suspend fun deleteCounter(@Body id: GeneralCounterRequest): List<CounterResponse>

    companion object {
        private const val BASE_ENDPOINT = "api/v1"
    }
}