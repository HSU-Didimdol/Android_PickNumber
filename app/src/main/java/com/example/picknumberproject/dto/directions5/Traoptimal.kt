package com.example.picknumberproject.dto.directions5

data class Traoptimal(
    val guide: List<Guide>,
    val path: List<List<Double>>,
    val section: List<Section>,
    val summary: Summary
)