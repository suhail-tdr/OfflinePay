package com.offlinepay.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
abstract class OfflinePayDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: OfflinePayDatabase? = null

        fun getDatabase(context: Context): OfflinePayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OfflinePayDatabase::class.java,
                    "offline_pay_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
