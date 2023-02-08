package com.example.data.model.directions5

data class SummaryDto(
    val bbox: List<List<Double>>,
    val departureTime: String,
    val distance: Int,
    val duration: Int,
    val etaServiceType: Int,
    val fuelPrice: Int,
    val goalDto: GoalDto,
    val startDto: StartDto,
    val taxiFare: Int,
    val tollFare: Int
)