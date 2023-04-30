package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.db.CompanyDao
import com.example.picknumberproject.data.dto.company.toEntity
import com.example.picknumberproject.data.dto.directions5.toEntity
import com.example.picknumberproject.data.source.CompanyRemoteDataSource
import com.example.picknumberproject.data.source.Directions5RemoteDataSource
import com.example.picknumberproject.domain.model.CompanyEntity
import com.example.picknumberproject.domain.model.DirectionEntity
import com.example.picknumberproject.domain.repository.CompanyRepository
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyRemoteDataSource: CompanyRemoteDataSource,
    private val directions5RemoteDataSource: Directions5RemoteDataSource,
    private val companyDao: CompanyDao
) : CompanyRepository {

    override suspend fun getAllCompanyEntityList(): Result<Unit> {
        return runCatching {
            val response = companyRemoteDataSource.getCompanyList()
            val companyList = response.body()!!.results.map { it.toEntity() }
            companyDao.insertAll(companyList)
        }
    }

    override suspend fun searchCompanyListByQuery(
        query: String,
        myLocation: String
    ): Result<List<CompanyEntity>> {
        return runCatching {
            val companyList = companyDao.searchQuery(query = query)
            val companyListDirections = companyList.map {
                val goal = "${it.longitude},${it.latitude}"
                val directionEntity = getDistanceAndDuration(start = myLocation, goal = goal)
                if (directionEntity.isSuccess) {
                    it.copy(
                        duration = directionEntity.getOrNull()!!.duration.toString(),
                        distance = directionEntity.getOrNull()!!.distance.toString()
                    )
                } else {
                    it.copy(
                        duration = "0",
                        distance = "0"
                    )
                }
            }
            companyListDirections
        }
    }

    override suspend fun getCompanyEntity(query: String): Result<CompanyEntity> {
        return runCatching {
            val phoneNumber = companyDao.searchValidCode(query = query)
            phoneNumber
        }
    }

    override suspend fun getDistanceAndDuration(
        start: String,
        goal: String
    ): Result<DirectionEntity> {
        return runCatching {
            val response = directions5RemoteDataSource.getDirections5(start = start, goal = goal)
            val body = response.body()
            check(body != null) { "body 응답이 없습니다." }
            body.toEntity()
        }
    }
}



