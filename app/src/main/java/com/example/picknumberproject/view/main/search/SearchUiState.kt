package com.example.picknumberproject.view.main.search

import com.example.picknumberproject.domain.model.CompanyEntity

data class SearchUiState(
    val companyListData: List<CompanyEntity> = emptyList(),
    val userMessage: Int? = null
)

data class SearchItemUiState(
    val companyID: Int
)