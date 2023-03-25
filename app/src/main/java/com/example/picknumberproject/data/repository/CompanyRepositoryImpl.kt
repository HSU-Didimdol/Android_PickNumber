package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.dto.company.toEntity
import com.example.picknumberproject.data.extension.getDataOrThrowMessage
import com.example.picknumberproject.data.source.CompanyLocalDataSource
import com.example.picknumberproject.data.source.CompanyRemoteDataSource
import com.example.picknumberproject.data.source.Directions5RemoteDataSource
import com.example.picknumberproject.domain.repository.CompanyRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyRemoteDataSource: CompanyRemoteDataSource,
    private val directions5RemoteDataSource: Directions5RemoteDataSource,
    private val companyLocalDataSource: CompanyLocalDataSource
) : CompanyRepository {


    init {
        MainScope().launch {
            getAllCompanyEntityList()
        }
    }

    override suspend fun getAllCompanyEntityList() {
        runCatching {
            val response = companyRemoteDataSource.getCompanyList()
            val companyList = response.getDataOrThrowMessage().companysDto.map { it.toEntity() }
            companyLocalDataSource.setData(companyList)
        }
    }


}
