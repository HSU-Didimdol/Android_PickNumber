package com.example.picknumberproject.dto.directions5

data class DirectionsDto(
    val code: Int,
    val currentDateTime: String,
    val message: String,
    val routeDto: RouteDto
)