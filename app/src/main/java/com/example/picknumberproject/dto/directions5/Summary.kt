package com.example.picknumberproject.dto.directions5

data class Summary(
    val bbox: List<List<Double>>,
    val departureTime: String,
    val distance: Double,
    val duration: Int,
    val etaServiceType: Int,
    val fuelPrice: Int,
    val goal: Goal,
    val start: Start,
    val taxiFare: Int,
    val tollFare: Int
)