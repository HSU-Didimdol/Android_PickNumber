package com.example.picknumberproject.domain.repository

import com.example.picknumberproject.domain.model.CompanyEntity
import com.example.picknumberproject.domain.model.DirectionEntity
import kotlinx.coroutines.flow.StateFlow

interface CompanyRepository {

    suspend fun getAllCompanyEntityList(): Result<Unit>

    suspend fun searchCompanyListByQuery(query: String): Result<List<CompanyEntity>>

    suspend fun getCompanyEntity(query: String): Result<CompanyEntity>

    suspend fun getDistanceAndDuration(
        start: String,
        goal: String
    ): Result<DirectionEntity>

}