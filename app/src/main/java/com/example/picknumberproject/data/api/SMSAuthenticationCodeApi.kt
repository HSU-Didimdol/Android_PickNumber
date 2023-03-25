package com.example.picknumberproject.data.api

import com.example.picknumberproject.data.dto.sms.SMSRequestBody
import com.example.picknumberproject.data.dto.sms.SMSResponseModel
import com.example.picknumberproject.data.url.Url
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SMSAuthenticationCodeApi {
    @POST(Url.SMS_AUTHENTICATION_URL)
    fun getCode(
        @Header("x-access-token") token: String,
        @Body requestBody: SMSRequestBody
    ): Call<SMSResponseModel>
}