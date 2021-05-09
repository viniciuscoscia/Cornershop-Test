package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.repository.CounterRepository
import com.cornershop.counterstest.domain.entities.Counter

class CreateCounterUseCase(
    private val repository: CounterRepository
) {
    suspend operator fun invoke(title: String): ResultWrapper<List<Counter>> {
        return repository.addCounter(title)
    }
}