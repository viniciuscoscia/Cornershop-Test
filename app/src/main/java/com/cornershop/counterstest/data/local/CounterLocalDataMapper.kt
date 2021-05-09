package com.cornershop.counterstest.data.local

import com.cornershop.counterstest.data.local.entity.CounterDatabaseEntity
import com.cornershop.counterstest.domain.entities.Counter

fun List<CounterDatabaseEntity>.toDomainEntity() = map { databaseEntity ->
    Counter(
        id = databaseEntity.id,
        title = databaseEntity.title,
        count = databaseEntity.count
    )
}

fun Counter.toDatabaseEntity() = CounterDatabaseEntity(
    id = id,
    title = title,
    count = count
)

fun List<Counter>.toDatabaseEntity() = map { counter ->
    CounterDatabaseEntity(
        id = counter.id,
        title = counter.title,
        count = counter.count
    )
}