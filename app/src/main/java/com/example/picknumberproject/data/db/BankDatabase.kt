package com.example.picknumberproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.picknumberproject.domain.model.BankEntity

@Database(entities = [BankEntity::class], version = 1)
abstract class BankDatabase : RoomDatabase() {
    abstract fun getBankDao(): BankDao

    companion object {
        private var INSTANCE: BankDatabase? = null

        fun getDatabase(context: Context): BankDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context, BankDatabase::class.java, "bank_table"
                )
                    .build()

            }
            return INSTANCE as BankDatabase
        }
    }
}