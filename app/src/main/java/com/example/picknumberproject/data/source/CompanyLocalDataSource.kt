package com.example.picknumberproject.data.source

import com.example.picknumberproject.domain.model.CompanyEntity

interface CompanyLocalDataSource {
    fun hasData(): Boolean
    fun getData(): List<CompanyEntity>?
    suspend fun setData(companyLocalData: List<CompanyEntity>)
    suspend fun clear()
}