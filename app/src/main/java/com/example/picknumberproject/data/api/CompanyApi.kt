package com.example.picknumberproject.data.api

import com.example.picknumberproject.data.dto.company.CompanysDto
import com.example.picknumberproject.data.url.Key
import com.example.picknumberproject.data.url.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface CompanyApi {

    @GET(Url.GET_COMPANY)
    suspend fun getCompanyList(
        @Header("x-access-token") x_access_token: String = Key.x_access_token
    ): Response<CompanysDto>
}