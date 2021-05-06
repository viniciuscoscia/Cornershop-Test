package com.cornershop.counterstest.presentation.mapper

import com.cornershop.counterstest.domain.entities.Counter
import com.cornershop.counterstest.presentation.model.CounterUiModel

fun List<Counter>.toUIModel() = map { counter ->
    CounterUiModel(
        id = counter.id,
        title = counter.title,
        count = counter.count
    )
}