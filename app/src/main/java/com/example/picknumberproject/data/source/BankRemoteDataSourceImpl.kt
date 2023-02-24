package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.api.BankApi
import com.example.picknumberproject.data.di.annotation.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BankRemoteDataSourceImpl @Inject constructor(
    private val bankApi: BankApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BankRemoteDataSource {

    override suspend fun getBankList() = withContext(ioDispatcher) {
        val response = bankApi.getBankList()
        if (response.isSuccessful && response.body() != null) {
            requireNotNull(response.body())
        } else {
            emptyList()
        }
    }

}