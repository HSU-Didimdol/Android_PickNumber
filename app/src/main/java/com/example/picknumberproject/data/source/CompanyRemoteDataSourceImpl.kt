package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.api.CompanyApi
import javax.inject.Inject

class CompanyRemoteDataSourceImpl @Inject constructor(
    private val companyApi: CompanyApi
) : CompanyRemoteDataSource {

    override suspend fun getCompanyList() = companyApi.getCompanyList()

}