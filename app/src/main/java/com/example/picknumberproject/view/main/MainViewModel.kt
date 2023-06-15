package com.example.picknumberproject.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.domain.model.CompanyEntity
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
class MainViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun updateCurrentLanLat(latitude: Double, longitude: Double, tag: Overlay?) {
        _uiState.update {
            it.copy(
                currentCameraLatitude = latitude,
                currentCameraLongitude = longitude,
                currentState = tag
            )
        }
    }

    fun updateSearchToMap() {
        _uiState.update {
            it.copy(searchToMap = true)
        }
    }

    fun updateMapToSearch() {
        _uiState.update {
            it.copy(searchToMap = false)
        }
    }

    fun notValidCurrentState(): Boolean {
        return uiState.value.currentState == null
    }

    fun mapBind(companyList: List<CompanyEntity>) {
        if (companyList.isNotEmpty()) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(companyListData = companyList)
                }
            }
        }
    }

    fun bind(query: String, myLocation: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
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
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}