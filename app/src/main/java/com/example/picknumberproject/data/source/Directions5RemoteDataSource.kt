package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.dto.directions5.Directions5Dto
import retrofit2.Response

interface Directions5RemoteDataSource {

    suspend fun getDirections5(
        start: String,
        goal: String
    ): Response<Directions5Dto>

}