package com.cornershop.counterstest.data.local.dao

import androidx.room.*
import com.cornershop.counterstest.data.local.entity.CounterEntity

@Dao
interface CounterDAO {
    @Query("SELECT * FROM counters")
    suspend fun getAll(): List<CounterEntity>

    @Transaction
    suspend fun updateData(users: List<CounterEntity>) {
        deleteAll()
        insertAll(users)
    }

    @Update
    suspend fun updateCounter(counter: CounterEntity)

    @Insert
    suspend fun insertAll(counters: List<CounterEntity>)

    @Query("DELETE FROM counters")
    suspend fun deleteAll()
}