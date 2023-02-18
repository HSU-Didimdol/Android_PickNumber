package com.example.picknumberproject.view.map

import com.example.picknumberproject.domain.model.BankEntity

data class MapUiState(
    val bankListData: List<BankEntity> = emptyList()
)