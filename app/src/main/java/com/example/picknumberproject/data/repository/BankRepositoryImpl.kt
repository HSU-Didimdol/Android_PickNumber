package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.db.BankDatabase
import com.example.picknumberproject.data.source.BankRemoteDataSource
import com.example.picknumberproject.data.source.Directions5RemoteDataSource
import com.example.picknumberproject.domain.model.BankEntity
import com.example.picknumberproject.domain.model.toEntity
import com.example.picknumberproject.domain.repository.BankRepository
import javax.inject.Inject

class BankRepositoryImpl @Inject constructor(
    private val bankRemoteDataSource: BankRemoteDataSource,
    private val directions5RemoteDataSource: Directions5RemoteDataSource,
    private val bankDB: BankDatabase
) : BankRepository {

    override suspend fun getAllBankEntityList(): List<BankEntity> {
        val response = bankRemoteDataSource.getBankList()
        val dataList = directions5RemoteDataSource.getBankDistance(response.map { it.toEntity() })
        bankDB.getBankDao().insertAll(dataList)
        return dataList
    }
}
