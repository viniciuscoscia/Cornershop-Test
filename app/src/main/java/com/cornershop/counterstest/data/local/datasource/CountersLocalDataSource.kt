package com.cornershop.counterstest.data.local.datasource

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.local.dao.CounterDAO
import com.cornershop.counterstest.data.local.toDomainEntity
import com.cornershop.counterstest.domain.entities.Counter

interface CountersLocalDataSource {
    suspend fun getCounters(): ResultWrapper<List<Counter>>
    suspend fun insertCounter(counter: Counter)
    suspend fun deleteCounter(counter: Counter)
    suspend fun updateCounter(counter: Counter)
    suspend fun updateDatabase(counters: List<Counter>)
}

class CountersLocalDataSourceImpl(
    private val countersDao: CounterDAO
) : CountersLocalDataSource {
    override suspend fun getCounters(): ResultWrapper<List<Counter>> {
        return try {
            ResultWrapper.Success(countersDao.getAll().toDomainEntity())
        } catch (e: Throwable) {
            ResultWrapper.DatabaseError
        }
    }

    override suspend fun insertCounter(counter: Counter) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCounter(counter: Counter) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCounter(counter: Counter) {
        TODO("Not yet implemented")
    }

    override suspend fun updateDatabase(counters: List<Counter>) {
        TODO("Not yet implemented")
    }
}