package com.example.picknumberproject.data.db

import androidx.room.*
import com.example.picknumberproject.domain.model.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun getAll(): List<UserEntity>

    @Query("SELECT phone FROM user_table")
    fun getPhone(): List<String>

    @Query("SELECT name FROM user_table WHERE phone= :phone")
    fun getUserByPhone(phone: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)
}