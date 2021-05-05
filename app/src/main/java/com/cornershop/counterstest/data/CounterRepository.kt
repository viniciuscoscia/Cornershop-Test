package com.cornershop.counterstest.data

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.remote.datasource.CounterRemoteDataSource
import com.cornershop.counterstest.domain.entities.Counter

interface CounterRepository {
    suspend fun fetchCounters(): ResultWrapper<List<Counter>>
}

class CounterRepositoryImpl(
    private val remoteDataSource: CounterRemoteDataSource
) : CounterRepository {
    override suspend fun fetchCounters(): ResultWrapper<List<Counter>> {
        return remoteDataSource.getCounters()
    }
}