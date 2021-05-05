package com.cornershop.counterstest.data.remote.api

import com.cornershop.counterstest.data.remote.entity.CounterResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface CounterAPI {
    @GET("/counters")
    suspend fun getCounters(): List<CounterResponse>

    @POST("/counter")
    suspend fun postAddCounter(title: String): List<CounterResponse>

    @POST("/counter/inc")
    suspend fun postIncreaseCounter(id: String): List<CounterResponse>

    @POST("/counter/dec")
    suspend fun postDecreaseCounter(id: String): List<CounterResponse>

    @DELETE("/counter")
    suspend fun deleteCounter(id: String): List<CounterResponse>
}