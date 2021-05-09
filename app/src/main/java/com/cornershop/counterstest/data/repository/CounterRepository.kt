package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.local.datasource.CountersLocalDataSource
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
    private val localDataSource: CountersLocalDataSource
) : CounterRepository {
    override suspend fun getCounters(): ResultWrapper<List<Counter>> {
        val localCounters = localDataSource.getCounters()

        if (localCounters.isNotEmpty()) {
            return ResultWrapper.Success(localCounters)
        }

        return remoteDataSource.getCounters()
    }

    override suspend fun addCounter(title: String): ResultWrapper<List<Counter>> {
        return checkResponseAndUpdateDatabase(remoteDataSource.addCounter(title))
    }

    override suspend fun increaseCounter(counterId: String): ResultWrapper<List<Counter>> {
        return checkResponseAndUpdateDatabase(remoteDataSource.increaseCounter(counterId))
    }

    override suspend fun decreaseCounter(counterId: String): ResultWrapper<List<Counter>> {
        return checkResponseAndUpdateDatabase(remoteDataSource.decreaseCounter(counterId))
    }

    override suspend fun deleteCounter(counterId: String): ResultWrapper<List<Counter>> {
        return checkResponseAndUpdateDatabase(remoteDataSource.deleteCounter(counterId))
    }

    private suspend fun checkResponseAndUpdateDatabase(
        result: ResultWrapper<List<Counter>>
    ): ResultWrapper<List<Counter>> {
        if (result is ResultWrapper.Success) {
            localDataSource.updateDatabase(result.value)
        }

        return result
    }
}