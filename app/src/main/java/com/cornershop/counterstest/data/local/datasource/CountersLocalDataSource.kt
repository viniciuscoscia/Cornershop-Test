package com.cornershop.counterstest.data.local.datasource

import com.cornershop.counterstest.data.local.dao.CounterDAO
import com.cornershop.counterstest.data.local.toDatabaseEntity
import com.cornershop.counterstest.data.local.toDomainEntity
import com.cornershop.counterstest.domain.entities.Counter

interface CountersLocalDataSource {
    suspend fun getCounters(): List<Counter>
    suspend fun insertCounter(counter: Counter)
    suspend fun insertCounters(counters: List<Counter>)
    suspend fun deleteCounter(counter: Counter)
    suspend fun updateCounter(counter: Counter)
    suspend fun updateDatabase(counters: List<Counter>)
    suspend fun searchByText(searchText: String): List<Counter>
}

class CountersLocalDataSourceImpl(
    private val countersDao: CounterDAO
) : CountersLocalDataSource {
    override suspend fun getCounters(): List<Counter> {
        return countersDao.getAll().toDomainEntity()
    }

    override suspend fun insertCounter(counter: Counter) {
        countersDao.insert(counter.toDatabaseEntity())
    }

    override suspend fun insertCounters(counters: List<Counter>) {
        countersDao.insertAll(counters.toDatabaseEntity())
    }

    override suspend fun deleteCounter(counter: Counter) {
        countersDao.delete(counter.toDatabaseEntity())
    }

    override suspend fun updateCounter(counter: Counter) {
        countersDao.update(counter.toDatabaseEntity())
    }

    override suspend fun updateDatabase(counters: List<Counter>) {
        countersDao.updateData(counters.toDatabaseEntity())
    }

    override suspend fun searchByText(searchText: String): List<Counter> {
        return countersDao.searchByText("%$searchText%").toDomainEntity()
    }
}