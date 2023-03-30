package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.dto.company.CompanysDto
import com.example.picknumberproject.data.extension.ResponseBody
import retrofit2.Response

interface CompanyRemoteDataSource {

    suspend fun getCompanyList(): Response<CompanysDto>

}