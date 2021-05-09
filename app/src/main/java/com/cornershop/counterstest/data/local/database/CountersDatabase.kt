package com.cornershop.counterstest.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cornershop.counterstest.data.local.dao.CounterDAO
import com.cornershop.counterstest.data.local.entity.CounterDatabaseEntity

@Database(version = 1, entities = [CounterDatabaseEntity::class])
abstract class CountersDatabase : RoomDatabase() {
    abstract fun countersDao(): CounterDAO

    companion object {
        private const val COUNTERS_DATABASE_FILE_NAME = "counters.db"

        fun createDatabase(context: Context): CounterDAO {
            return Room
                .databaseBuilder(
                    context,
                    CountersDatabase::class.java,
                    COUNTERS_DATABASE_FILE_NAME
                )
                .build()
                .countersDao()
        }
    }
}