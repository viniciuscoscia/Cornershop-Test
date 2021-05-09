package com.cornershop.counterstest.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counters")
data class CounterDatabaseEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val title: String,
    @ColumnInfo val count: Int
)