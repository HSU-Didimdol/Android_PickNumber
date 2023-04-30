package com.example.picknumberproject.view.main.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.domain.repository.CompanyRepository
import com.naver.maps.map.overlay.Overlay
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

    fun updateCurrentState(tag: Overlay) {
        _uiState.update {
            it.copy(currentState = tag)
        }
    }

    fun updateCurrentLanLat(latitude: Double, longitude: Double) {
        _uiState.update {
            it.copy(currentCameraLatitude = latitude, currentCameraLongitude = longitude)
        }
    }

    fun notValidCurrentState(): Boolean {
        return uiState.value.currentState == null
    }

    fun bind(query: String, myLocation: String) {
        viewModelScope.launch {
            val result = companyRepository.searchCompanyListByQuery("%$query%", myLocation)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(companyListData = result.getOrThrow())
                }
            } else {
                _uiState.update {
                    it.copy(userMessage = result.exceptionOrNull()!!.localizedMessage!!.toInt())
                }
            }
        }
    }


    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}