package com.example.picknumberproject.view.main.reservationpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationPageViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(
        ReservationPageUiState()
    )
    val uiState = _uiState.asStateFlow()

    fun bind(
        url: String
    ) {
        viewModelScope.launch {
            val result = authRepository.getCurrentUserInfo()
            if (result.isSuccess) {
                val userInfo = result.getOrNull()
                _uiState.update {
                    it.copy(
                        url = url + "&name=${userInfo!![0].name}&phoneNumber=${userInfo[0].phone}"
                    )
                }
            }
        }
    }
}