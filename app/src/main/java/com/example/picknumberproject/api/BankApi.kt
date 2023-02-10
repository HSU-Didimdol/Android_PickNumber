package com.example.picknumberproject.api

import com.example.picknumberproject.dto.bank.BankDto
import com.example.picknumberproject.url.Url
import retrofit2.Response
import retrofit2.http.GET

interface BankApi {
    @GET(Url.GET_BANK_DATA)
    suspend fun getBankList(): Response<List<BankDto>>
}