package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.dto.company.CompanyDto
import retrofit2.Response

interface CompanyRemoteDataSource {

    suspend fun getCompanyList(): Response<CompanyDto>

}