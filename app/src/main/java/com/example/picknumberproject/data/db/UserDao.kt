package com.example.picknumberproject.data.db

import androidx.room.*
import com.example.picknumberproject.domain.model.UserEntity

/**
 * company_table에서 담기는 Dao를 담고있는 interface입니다.
 */

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun getAll(): List<UserEntity>

    @Query("SELECT phone FROM user_table")
    fun getPhone(): List<String>

    @Query("SELECT password FROM user_table")
    fun getPassword(): List<String>

    @Query("SELECT name FROM user_table WHERE phone= :phone")
    fun getUserByPhone(phone: String): String

    @Query("SELECT name FROM user_table WHERE password= :password")
    fun getUserByPassword(password: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)
}