package com.example.picknumberproject.view.main

import com.example.picknumberproject.domain.model.CompanyEntity
import com.naver.maps.map.overlay.Overlay

data class MainUiState(
    val companyListData: List<CompanyEntity> = emptyList(),
    val userMessage: Int? = null,
    val isLoading: Boolean = false,
    val currentState: Overlay? = null,
    val currentCameraLongitude: Double? = null,
    val currentCameraLatitude: Double? = null
)