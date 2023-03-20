package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.source.CompanyLocalDataSource
import com.example.picknumberproject.data.source.CompanyRemoteDataSource
import com.example.picknumberproject.data.source.Directions5RemoteDataSource
import com.example.picknumberproject.domain.model.CompanyEntity
import com.example.picknumberproject.domain.repository.CompanyRepository
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyRemoteDataSource: CompanyRemoteDataSource,
    private val directions5RemoteDataSource: Directions5RemoteDataSource,
    private val companyLocalDataSource: CompanyLocalDataSource
) : CompanyRepository {


    override suspend fun getAllCompanyEntityList(): List<CompanyEntity> {
        TODO("Not yet implemented")
    }

}
