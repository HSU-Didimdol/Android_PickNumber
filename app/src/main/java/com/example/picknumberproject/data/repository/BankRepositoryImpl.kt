package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.source.BankLocalDataSource
import com.example.picknumberproject.data.source.BankRemoteDataSource
import com.example.picknumberproject.data.source.Directions5RemoteDataSource
import com.example.picknumberproject.domain.model.BankEntity
import com.example.picknumberproject.domain.model.toEntity
import com.example.picknumberproject.domain.repository.BankRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class BankRepositoryImpl @Inject constructor(
    private val bankRemoteDataSource: BankRemoteDataSource,
    private val directions5RemoteDataSource: Directions5RemoteDataSource,
    private val bankLocalDataSource: BankLocalDataSource
) : BankRepository {

    init {
        MainScope().launch {
            try {
                if (bankLocalDataSource.hasData()) {
                    bankLocalDataSource.getData()
                } else {
                    getAllBankEntityList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 인터넷 연결이 끊김 등의 예외는 무시한다.
            }
        }
    }

    override suspend fun getAllBankEntityList(): List<BankEntity> {
        val response = bankRemoteDataSource.getBankList()
        val dataList = directions5RemoteDataSource.getBankDistance(response.map { it.toEntity() })
        bankLocalDataSource.setData(dataList)
        return dataList
    }
}
