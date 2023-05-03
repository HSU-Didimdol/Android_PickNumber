package com.example.picknumberproject.view.main.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.domain.model.CompanyEntity
import com.naver.maps.map.overlay.Overlay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun updateCurrentLanLat(latitude: Double, longitude: Double, tag: Overlay?) {
        _uiState.update {
            it.copy(
                currentCameraLatitude = latitude,
                currentCameraLongitude = longitude,
                currentState = tag
            )
        }
    }

    fun notValidCurrentState(): Boolean {
        return uiState.value.currentState == null
    }

    fun bind(companyList: List<CompanyEntity>) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(companyListData = companyList)
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}