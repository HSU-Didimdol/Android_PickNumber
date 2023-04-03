package com.example.picknumberproject.data.api

import com.example.picknumberproject.data.dto.directions5.Directions5Dto
import com.example.picknumberproject.data.url.Key
import com.example.picknumberproject.data.url.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Direction5Api의 interface 입니다. 거리와 시간 계산을 위한 getDistance를 담고 있고,
 * 이에 대한 Rrtrofit에 대한 객체는 Network 모듈에 담고있습니다.
 */

interface Direction5Api {
    @GET(Url.GET_DIRECTIONS5)
    suspend fun getDistance(
        @Header("X-NCP-APIGW-API-KEY-ID") clientID: String = Key.clientID,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String = Key.clientSecret,
        @Query("start", encoded = true) start: String,
        @Query("goal", encoded = true) goal: String,
    ): Response<Directions5Dto>
}