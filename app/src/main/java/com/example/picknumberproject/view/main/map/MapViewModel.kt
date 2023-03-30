package com.example.picknumberproject.view.main.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.R
import com.example.picknumberproject.domain.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        refreshData()
    }

    fun bind(query: String) {
        viewModelScope.launch {
            val result = companyRepository.searchCompanyListByQuery("%$query%")
            if (result.isSuccess) {
                _uiState.update { it.copy(companyListData = result.getOrNull()!!) }
            } else {
                _uiState.update { it.copy(userMessage = R.string.failed_data) }
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {

        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}