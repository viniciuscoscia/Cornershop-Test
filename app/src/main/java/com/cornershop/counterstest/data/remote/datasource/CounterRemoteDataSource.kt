package com.cornershop.counterstest.data.remote.datasource

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.helper.safeApiCall
import com.cornershop.counterstest.data.remote.api.CounterAPI
import com.cornershop.counterstest.data.remote.entity.AddCounterRequest
import com.cornershop.counterstest.data.remote.entity.GeneralCounterRequest
import com.cornershop.counterstest.data.remote.toDomainEntity
import com.cornershop.counterstest.domain.entities.Counter
import kotlinx.coroutines.Dispatchers

interface CounterRemoteDataSource {
    suspend fun getCounters(): ResultWrapper<List<Counter>>
    suspend fun addCounter(title: String): ResultWrapper<List<Counter>>
    suspend fun increaseCounter(counterId: String): ResultWrapper<List<Counter>>
    suspend fun decreaseCounter(counterId: String): ResultWrapper<List<Counter>>
    suspend fun deleteCounter(counterId: String): ResultWrapper<List<Counter>>
}

class CounterRemoteDataSourceImpl(
    private val counterAPI: CounterAPI
) : CounterRemoteDataSource {
    override suspend fun getCounters(): ResultWrapper<List<Counter>> {
        return safeApiCall(Dispatchers.IO) { counterAPI.getCounters().toDomainEntity() }
    }

    override suspend fun addCounter(title: String): ResultWrapper<List<Counter>> {
        return safeApiCall(Dispatchers.IO) {
            counterAPI.postAddCounter(AddCounterRequest(title)).toDomainEntity()
        }
    }

    override suspend fun increaseCounter(counterId: String): ResultWrapper<List<Counter>> {
        return safeApiCall(Dispatchers.IO) {
            counterAPI.postIncreaseCounter(GeneralCounterRequest(counterId)).toDomainEntity()
        }
    }

    override suspend fun decreaseCounter(counterId: String): ResultWrapper<List<Counter>> {
        return safeApiCall(Dispatchers.IO) {
            counterAPI.postDecreaseCounter(GeneralCounterRequest(counterId)).toDomainEntity()
        }
    }

    override suspend fun deleteCounter(counterId: String): ResultWrapper<List<Counter>> {
        return safeApiCall(Dispatchers.IO) {
            counterAPI.deleteCounter(GeneralCounterRequest(counterId)).toDomainEntity()
        }
    }
}