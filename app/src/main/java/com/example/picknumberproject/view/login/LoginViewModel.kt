package com.example.picknumberproject.view.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUserName(userName: String) {
        _uiState.update { it.copy(userName = userName) }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun signIn() {
        val name = uiState.value.userName
        val phoneNumber = uiState.value.phoneNumber
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authRepository.signIn(name, phoneNumber)
            if (result.isSuccess) {
                _uiState.update { it.copy(successToSignIn = true, isLoading = false) }
            } else {
                _uiState.update {
                    it.copy(
                        userMessage = result.exceptionOrNull()!!.localizedMessage,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun checkUserInfoExists(phoneNumber: String): Boolean {
        return authRepository.hasUserInfo(phoneNumber)
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}