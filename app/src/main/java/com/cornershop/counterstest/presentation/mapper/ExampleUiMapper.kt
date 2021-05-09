package com.cornershop.counterstest.presentation.mapper

import com.cornershop.counterstest.domain.entities.ExampleEntity
import com.cornershop.counterstest.presentation.model.ExampleUiModel

fun List<ExampleEntity>.toUIModel() = map { domainEntity ->
    ExampleUiModel(
        title = domainEntity.title,
        examples = domainEntity.examples
    )
}