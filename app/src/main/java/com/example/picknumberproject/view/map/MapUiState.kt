package com.example.picknumberproject.view.map

import com.example.picknumberproject.domain.model.CompanyEntity

data class MapUiState(
    val companyListData: List<CompanyEntity> = emptyList()
)