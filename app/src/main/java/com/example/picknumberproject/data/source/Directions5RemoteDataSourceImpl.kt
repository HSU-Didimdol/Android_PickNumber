package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.api.Direction5Api
import javax.inject.Inject

class Directions5RemoteDataSourceImpl @Inject constructor(
    private val direction5Api: Direction5Api
) : Directions5RemoteDataSource {

    override suspend fun getDirections5(
        start: String,
        goal: String
    ) = direction5Api.getDistance(
        start = start,
        goal = goal
    )
}