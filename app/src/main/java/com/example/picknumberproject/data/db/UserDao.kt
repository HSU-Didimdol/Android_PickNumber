package com.example.picknumberproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.picknumberproject.domain.model.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun getAll(): List<UserEntity>

    @Query("SELECT phone FROM user_table")
    fun getPhone(): List<String>

    @Query("SELECT name FROM user_table WHERE phone= :phone")
    fun getUserByPhone(phone: String): String

    @Insert
    fun insertUser(vararg user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)
}