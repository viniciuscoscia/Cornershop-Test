package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.local.datasource.ExamplesLocalDataSource
import com.cornershop.counterstest.domain.entities.ExampleEntity

interface ExamplesRepository {
    suspend fun getExamples(): List<ExampleEntity>
}

class ExamplesRepositoryImpl(
    private val examplesLocalDataSource: ExamplesLocalDataSource
) : ExamplesRepository {
    override suspend fun getExamples(): List<ExampleEntity> {
        return examplesLocalDataSource.getExamples()
    }
}