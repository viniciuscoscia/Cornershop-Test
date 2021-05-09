package com.cornershop.counterstest.domain.usecases

import com.cornershop.counterstest.data.repository.ExamplesRepository
import com.cornershop.counterstest.domain.entities.ExampleEntity

class GetExamplesUseCase(
    private val repository: ExamplesRepository
) {
    suspend operator fun invoke(): List<ExampleEntity> {
        return repository.getExamples()
    }
}