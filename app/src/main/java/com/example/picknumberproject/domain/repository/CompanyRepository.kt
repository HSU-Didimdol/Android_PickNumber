package com.example.picknumberproject.domain.repository

import com.example.picknumberproject.domain.model.CompanyEntity

interface CompanyRepository {

    suspend fun getAllCompanyEntityList(): List<CompanyEntity>

}