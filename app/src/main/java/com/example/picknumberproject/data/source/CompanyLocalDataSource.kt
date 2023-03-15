package com.example.picknumberproject.data.source

import com.example.picknumberproject.domain.model.CompanyEntity

interface CompanyLocalDataSource {
    fun hasData(): Boolean
    fun getData(): List<CompanyEntity>?
    suspend fun setData(bankLocalData: List<CompanyEntity>)
    suspend fun clear()
}