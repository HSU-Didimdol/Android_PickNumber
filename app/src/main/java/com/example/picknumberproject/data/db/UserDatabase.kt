package com.example.picknumberproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.picknumberproject.domain.model.UserEntity

/**
 * UserDatabase 대한 추상클래스입니다.
 * Companion Object를 통해 해당 내용을 추상클래스가 실행되었을때 바로 실행시켜줍니다.
 */

@Database(entities = [UserEntity::class], version = 2)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_table"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }
}