package com.cornershop.counterstest.data.remote

import com.cornershop.counterstest.data.remote.entity.CounterResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface CounterAPI {
    @GET("/counters")
    fun getCounters(): Response<List<CounterResponse>>

    @POST("/counter")
    fun postAddCounter(title: String): Response<List<CounterResponse>>

    @POST("/counter/inc")
    fun postIncreaseCounter(id: String): Response<List<CounterResponse>>

    @POST("/counter/dec")
    fun postDecreaseCounter(id: String): Response<List<CounterResponse>>

    @DELETE("/counter")
    fun deleteCounter(id: String): Response<List<CounterResponse>>
}