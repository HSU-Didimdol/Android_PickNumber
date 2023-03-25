package com.example.picknumberproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.picknumberproject.domain.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getDao(): UserDao
}