package com.example.picknumberproject.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.picknumberproject.dto.bank.BankDto
import com.example.picknumberproject.model.BankEntity

@Dao
interface BankDao {
    @Query("SELECT * FROM bank")
    fun getAll(): List<BankEntity>

    @Insert
    fun insertAll(users: List<BankEntity>)
}