package com.cornershop.counterstest.domain.usecases

import com.cornershop.counterstest.data.CounterRepository
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.entities.Counter

class CreateCounterUseCase(
    private val counterRepository: CounterRepository
) {
    suspend operator fun invoke(title: String): ResultWrapper<List<Counter>> {
        return counterRepository.addCounter(title)
    }
}