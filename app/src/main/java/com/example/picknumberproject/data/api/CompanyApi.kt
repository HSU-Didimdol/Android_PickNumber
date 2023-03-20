package com.example.picknumberproject.data.api

import com.example.picknumberproject.data.dto.company.CompanyDto
import com.example.picknumberproject.data.url.Key
import retrofit2.Response
import retrofit2.http.Header

interface CompanyApi {
    suspend fun getCompanyList(
        @Header("x-access-token") x_access_token: String = Key.x_access_token
    ): Response<CompanyDto>
}