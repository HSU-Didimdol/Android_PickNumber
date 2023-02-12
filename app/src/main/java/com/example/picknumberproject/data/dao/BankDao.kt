package com.example.picknumberproject.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.picknumberproject.domain.model.BankEntity

@Dao
interface BankDao {
    @Query("SELECT * FROM bank")
    fun getAll(): List<BankEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 같은 것이 충돌시에 새로운 것으로 대체한다.
    suspend fun insertAll(banks: List<BankEntity>)

    @Query("SELECT * FROM bank WHERE name = :keyWord")
    suspend fun getSearchKeyWord(keyWord: String): List<BankEntity>
}