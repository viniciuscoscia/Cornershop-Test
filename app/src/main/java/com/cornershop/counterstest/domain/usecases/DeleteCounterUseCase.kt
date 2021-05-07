package com.cornershop.counterstest.domain.usecases

import com.cornershop.counterstest.data.CounterRepository
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.entities.Counter

class DeleteCounterUseCase(
    private val repository: CounterRepository
) {
    suspend operator fun invoke(counterId: String): ResultWrapper<List<Counter>> {
        return repository.deleteCounter(counterId)
    }
}