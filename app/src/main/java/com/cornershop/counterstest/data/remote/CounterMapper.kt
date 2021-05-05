package com.cornershop.counterstest.data.remote

import com.cornershop.counterstest.data.remote.entity.CounterResponse
import com.cornershop.counterstest.domain.entities.Counter

fun List<CounterResponse>?.toDomainEntity(): List<Counter> {
    return this?.map { response ->
        Counter(
            id = response.id,
            title = response.title,
            count = response.count
        )
    } ?: listOf()
}