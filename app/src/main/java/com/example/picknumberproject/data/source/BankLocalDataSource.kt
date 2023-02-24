package com.example.picknumberproject.data.source

import com.example.picknumberproject.domain.model.BankEntity

interface BankLocalDataSource {
    fun hasData(): Boolean
    fun getData(): List<BankEntity>?
    suspend fun setData(bankLocalData: List<BankEntity>)
    suspend fun clear()
}