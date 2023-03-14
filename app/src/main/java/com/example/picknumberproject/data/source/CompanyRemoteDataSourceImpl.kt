package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.api.CompanyApi
import com.example.picknumberproject.data.di.annotation.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CompanyRemoteDataSourceImpl @Inject constructor(
    private val companyApi: CompanyApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyRemoteDataSource {

    override suspend fun getBankList() = withContext(ioDispatcher) {
        val response = companyApi.getBankList()
        if(response.isSuccessful && response.body() != null){

        }
    }

}