package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.source.CompanyLocalDataSource
import com.example.picknumberproject.data.source.CompanyRemoteDataSource
import com.example.picknumberproject.data.source.Directions5RemoteDataSource
import com.example.picknumberproject.domain.model.BankEntity
import com.example.picknumberproject.domain.model.toEntity
import com.example.picknumberproject.domain.repository.BankRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyRemoteDataSource: CompanyRemoteDataSource,
    private val directions5RemoteDataSource: Directions5RemoteDataSource,
    private val companyLocalDataSource: CompanyLocalDataSource
) : BankRepository {

    init {
        MainScope().launch {
            try {
                if (companyLocalDataSource.hasData()) {
                    companyLocalDataSource.getData()
                } else {
                    getAllBankEntityList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getAllBankEntityList(): List<BankEntity> {
        val response = companyRemoteDataSource.getBankList()
        val dataList = directions5RemoteDataSource.getBankDistance(response.map { it.toEntity() })
        companyLocalDataSource.setData(dataList)
        return dataList
    }
}
