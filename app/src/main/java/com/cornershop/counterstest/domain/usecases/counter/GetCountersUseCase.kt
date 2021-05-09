package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.repository.CounterRepository
import com.cornershop.counterstest.domain.entities.Counter

class GetCountersUseCase(
    private val repository: CounterRepository
) {
    suspend operator fun invoke(): ResultWrapper<List<Counter>> {
        return repository.getCounters()
    }
}