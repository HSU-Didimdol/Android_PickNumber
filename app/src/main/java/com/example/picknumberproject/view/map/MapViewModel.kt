package com.example.picknumberproject.view.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.domain.repository.BankRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val bankRepository: BankRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(bankListData = bankRepository.getAllBankEntityList())
            }
        }
    }
}