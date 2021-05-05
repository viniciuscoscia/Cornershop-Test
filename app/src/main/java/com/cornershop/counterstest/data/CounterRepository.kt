package com.cornershop.counterstest.data

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.entities.Counter

interface CounterRepository {
    fun fetchCounters(): ResultWrapper<List<Counter>?>
}

class CounterRepositoryImpl : CounterRepository {
    override fun fetchCounters(): ResultWrapper<List<Counter>?> {
        return ResultWrapper.Success(listOf())
    }
}