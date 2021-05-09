package com.cornershop.counterstest.data.local.dao

import androidx.room.*
import com.cornershop.counterstest.data.local.entity.CounterDatabaseEntity

@Dao
interface CounterDAO {
    @Query("SELECT * FROM counters")
    suspend fun getAll(): List<CounterDatabaseEntity>

    @Transaction
    suspend fun updateData(counters: List<CounterDatabaseEntity>) {
        deleteAll()
        insertAll(counters)
    }

    @Update
    suspend fun update(counter: CounterDatabaseEntity)

    @Insert
    suspend fun insertAll(counters: List<CounterDatabaseEntity>)

    @Insert
    suspend fun insert(counter: CounterDatabaseEntity)

    @Query("DELETE FROM counters")
    suspend fun deleteAll()

    @Delete
    fun delete(counter: CounterDatabaseEntity)
}