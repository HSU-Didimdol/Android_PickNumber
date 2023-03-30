package com.example.picknumberproject.domain.repository

import com.example.picknumberproject.domain.model.CompanyEntity

interface CompanyRepository {

    suspend fun getAllCompanyEntityList(): Result<Unit>

    suspend fun searchCompanyListByQuery(query: String): Result<List<CompanyEntity>>

    suspend fun getValidCode(query: String): Result<String>

}