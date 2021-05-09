package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.remote.datasource.CounterRemoteDataSource
import com.cornershop.counterstest.domain.entities.Counter

interface CounterRepository {
    suspend fun getCounters(): ResultWrapper<List<Counter>>
    suspend fun addCounter(title: String): ResultWrapper<List<Counter>>
    suspend fun increaseCounter(counterId: String): ResultWrapper<List<Counter>>
    suspend fun decreaseCounter(counterId: String): ResultWrapper<List<Counter>>
    suspend fun deleteCounter(counterId: String): ResultWrapper<List<Counter>>
}

class CounterRepositoryImpl(
    private val remoteDataSource: CounterRemoteDataSource,
) : CounterRepository {
    override suspend fun getCounters(): ResultWrapper<List<Counter>> {
        return remoteDataSource.getCounters()
    }

    override suspend fun addCounter(title: String): ResultWrapper<List<Counter>> {
        return remoteDataSource.addCounter(title)
    }

    override suspend fun increaseCounter(counterId: String): ResultWrapper<List<Counter>> {
        return remoteDataSource.increaseCounter(counterId)
    }

    override suspend fun decreaseCounter(counterId: String): ResultWrapper<List<Counter>> {
        return remoteDataSource.decreaseCounter(counterId)
    }

    override suspend fun deleteCounter(counterId: String): ResultWrapper<List<Counter>> {
        return remoteDataSource.deleteCounter(counterId)
    }
}