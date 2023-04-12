package com.example.picknumberproject.data.dto.directions5

import com.example.picknumberproject.domain.model.DirectionEntity

data class Directions5Dto(
    val code: Int,
    val currentDateTime: String,
    val message: String,
    val route: Route
)

fun Directions5Dto.toEntity() = DirectionEntity(
    duration = route.traoptimal[0].summary.duration / 1000 / 60,
    distance = route.traoptimal[0].summary.distance / 1000
)