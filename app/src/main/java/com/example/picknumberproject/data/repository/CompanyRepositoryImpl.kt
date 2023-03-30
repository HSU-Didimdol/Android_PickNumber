package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.db.CompanyDao
import com.example.picknumberproject.data.dto.company.toEntity
import com.example.picknumberproject.data.source.CompanyRemoteDataSource
import com.example.picknumberproject.domain.model.CompanyEntity
import com.example.picknumberproject.domain.repository.CompanyRepository
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyRemoteDataSource: CompanyRemoteDataSource,
    private val companyDao: CompanyDao
) : CompanyRepository {

    override suspend fun getAllCompanyEntityList(): Result<Unit> {
        return runCatching {
            val response = companyRemoteDataSource.getCompanyList()
            val companyList = response.body()!!.results.map { it.toEntity() }
            companyDao.insertAll(companyList)
        }
    }

    override suspend fun searchCompanyListByQuery(query: String): Result<List<CompanyEntity>> {
        return runCatching {
            val companyList = companyDao.searchQuery(query = query)
            companyList
        }
    }

    override suspend fun getValidCode(query: String): Result<String> {
        return runCatching {
            val code = companyDao.searchValidCode(query = query).securityKey
            code
        }
    }

}
