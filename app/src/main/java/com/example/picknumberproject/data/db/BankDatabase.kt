package com.example.picknumberproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.picknumberproject.data.dao.BankDao
import com.example.picknumberproject.domain.model.BankEntity

@Database(entities = [BankEntity::class], version = 1)
abstract class BankDatabase : RoomDatabase() {
    abstract fun getBankDao(): BankDao

    companion object {
        private var INSTANCE: BankDatabase? = null
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE bank_table ADD COLUMN last_update INTEGER")
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE bank_table ADD COLUMN last_update INTEGER")
            }
        }

        fun getDatabase(context: Context): BankDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context, BankDatabase::class.java, "bank_table"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()

            }
            return INSTANCE as BankDatabase
        }
    }
}