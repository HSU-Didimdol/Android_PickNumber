package com.example.picknumberproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.picknumberproject.domain.model.CompanyEntity

/**
 * CompanyDataBase에 대한 추상클래스입니다.
 * Companion Object를 통해 해당 내용을 추상클래스가 실행되었을때 바로 실행시켜줍니다.
 */

@Database(entities = [CompanyEntity::class], version = 1)
abstract class CompanyDatabase : RoomDatabase() {
    abstract fun getDao(): CompanyDao

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