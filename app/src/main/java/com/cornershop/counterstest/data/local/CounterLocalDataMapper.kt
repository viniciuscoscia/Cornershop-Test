package com.cornershop.counterstest.data.local

import com.cornershop.counterstest.data.local.entity.CounterEntity
import com.cornershop.counterstest.domain.entities.Counter

fun List<CounterEntity>.toDomainEntity() = map { databaseEntity ->
    Counter(
        id = databaseEntity.id,
        title = databaseEntity.title,
        count = databaseEntity.count
    )
}