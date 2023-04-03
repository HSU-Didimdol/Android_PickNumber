package com.example.picknumberproject.data.api

import com.example.picknumberproject.data.dto.company.CompanysDto
import com.example.picknumberproject.data.url.Key
import com.example.picknumberproject.data.url.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * CompanyApi의 interface 입니다. 회사목록을 불러오는 getCompanyList를 담고 있고,
 * 이에 대한 Rrtrofit에 대한 객체는 Network 모듈에 담고있습니다.
 */

interface CompanyApi {

    @GET(Url.GET_COMPANY)
    suspend fun getCompanyList(
        @Header("x-access-token") x_access_token: String = Key.x_access_token
    ): Response<CompanysDto>
}