package com.example.picknumberproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.picknumberproject.domain.model.BankEntity

@Database(entities = [BankEntity::class], version = 1)
abstract class CompanyDatabase : RoomDatabase() {
    abstract fun getBankDao(): CompanyDao

    companion object {
        @Volatile
        private var INSTANCE: CompanyDatabase? = null

        fun getDatabase(context: Context): CompanyDatabase {
            if (INSTANCE == null) {
                synchronized(CompanyDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CompanyDatabase::class.java,
                        "company_table"
                    )
                        .fallbackToDestructiveMigration()
                        .build();
                }
            }
            return INSTANCE as CompanyDatabase
        }
    }
}