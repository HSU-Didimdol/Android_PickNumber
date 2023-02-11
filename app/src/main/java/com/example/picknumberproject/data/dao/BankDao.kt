package com.example.picknumberproject.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.picknumberproject.domain.model.BankEntity

@Dao
interface BankDao {
    @Query("SELECT * FROM bank")
    fun getAll(): LiveData<List<BankEntity>>

    @Insert
    suspend fun insertAll(banks: List<BankEntity>)

    @Query("SELECT * FROM bank WHERE name = :keyWord")
    suspend fun getSearchKeyWord(keyWord: String): List<BankEntity>
}