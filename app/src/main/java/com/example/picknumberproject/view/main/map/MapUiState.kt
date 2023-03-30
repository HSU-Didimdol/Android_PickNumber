package com.example.picknumberproject.view.main.map

import com.example.picknumberproject.domain.model.CompanyEntity

data class MapUiState(
    val companyListData: List<CompanyEntity> = emptyList(),
    val userMessage: String? = null
)