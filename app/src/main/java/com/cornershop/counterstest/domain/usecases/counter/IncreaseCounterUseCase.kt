package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.repository.CounterRepository
import com.cornershop.counterstest.domain.entities.Counter

class IncreaseCounterUseCase(
    private val repository: CounterRepository
) {
    suspend operator fun invoke(counterId: String): ResultWrapper<List<Counter>> {
        return repository.increaseCounter(counterId)
    }
}