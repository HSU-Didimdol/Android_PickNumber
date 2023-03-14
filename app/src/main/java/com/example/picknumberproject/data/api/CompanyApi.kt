package com.example.picknumberproject.data.api

import com.example.picknumberproject.data.dto.company.CompanyDto
import retrofit2.Response

interface CompanyApi {
    suspend fun getBankList(): Response<CompanyDto>
}