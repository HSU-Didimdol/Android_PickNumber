package com.example.picknumberproject.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.R
import com.example.picknumberproject.domain.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun bind() {
        viewModelScope.launch {
            val result = companyRepository.getAllCompanyEntityList()
            _uiState.update {
                it.copy(
                    userMessage = if (result.isSuccess) {
                        R.string.success_data
                    } else {
                        R.string.failed_data
                    }
                )
            }
        }
    }

}