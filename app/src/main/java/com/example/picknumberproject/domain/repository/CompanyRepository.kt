package com.example.picknumberproject.domain.repository

import com.example.picknumberproject.domain.model.CompanyEntity
import com.example.picknumberproject.domain.model.DirectionEntity

interface CompanyRepository {

    suspend fun getAllCompanyEntityList(): Result<Unit>

    suspend fun searchCompanyListByQuery(
        query: String,
        myLocation: String
    ): Result<List<CompanyEntity>>

    suspend fun getCompanyEntity(query: String): Result<CompanyEntity>

    suspend fun getDistanceAndDuration(
        start: String,
        goal: String
    ): Result<DirectionEntity>

}