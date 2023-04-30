package com.example.picknumberproject.view.main.map

import com.example.picknumberproject.domain.model.CompanyEntity
import com.naver.maps.map.overlay.Overlay

data class MapUiState(
    val companyListData: List<CompanyEntity> = emptyList(),
    val userMessage: Int? = null,
    val currentState: Overlay? = null,
    val currentCameraLongitude: Double? = null,
    val currentCameraLatitude: Double? = null
)


