package com.example.picknumberproject.view.main.homepage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomePageUiState()
    )
    val uiState = _uiState.asStateFlow()

    fun bind(
        url: String
    ) {
        _uiState.update { it.copy(url = url) }
    }
}