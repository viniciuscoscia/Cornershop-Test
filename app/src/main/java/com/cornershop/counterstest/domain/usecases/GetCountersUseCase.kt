package com.cornershop.counterstest.domain.usecases

import com.cornershop.counterstest.data.CounterRepository
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.entities.Counter

class GetCountersUseCase(
    private val repository: CounterRepository
) {
    suspend operator fun invoke(): ResultWrapper<List<Counter>> {
        return repository.fetchCounters()
    }
}