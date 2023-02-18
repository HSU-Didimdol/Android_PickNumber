package com.example.picknumberproject.view

import com.example.picknumberproject.domain.model.BankEntity

data class MainUiState(
    val bankListData: List<BankEntity> = emptyList()
)