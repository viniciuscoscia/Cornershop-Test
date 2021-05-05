package com.cornershop.counterstest.data.remote.datasource

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.helper.safeApiCall
import com.cornershop.counterstest.data.remote.api.CounterAPI
import com.cornershop.counterstest.data.remote.toDomainEntity
import com.cornershop.counterstest.domain.entities.Counter
import kotlinx.coroutines.Dispatchers

interface CounterRemoteDataSource {
    suspend fun getCounters(): ResultWrapper<List<Counter>>
}

class CounterRemoteDataSourceImpl(
    private val counterAPI: CounterAPI
) : CounterRemoteDataSource {
    override suspend fun getCounters(): ResultWrapper<List<Counter>> {
        return safeApiCall(Dispatchers.IO) { counterAPI.getCounters().toDomainEntity() }
    }
}