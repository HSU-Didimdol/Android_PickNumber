package com.example.data.model.directions5

data class DirectionsDto(
    val code: Int,
    val currentDateTime: String,
    val message: String,
    val routeDto: RouteDto
)