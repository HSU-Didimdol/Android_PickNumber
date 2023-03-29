package com.example.picknumberproject.view.signup

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
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUserName(userName: String) {
        _uiState.update { it.copy(userName = userName) }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun updateAuthenticCode(authenticCode: String) {
        _uiState.update { it.copy(authenticCode = authenticCode) }
    }

    fun updateCheckBox(check: Boolean) {
        _uiState.update { it.copy(checkBox = check) }
    }

    fun signUp() {
        val name = uiState.value.userName
        val phoneNumber = uiState.value.phoneNumber
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authRepository.signUp(name, phoneNumber)
            if (result.isSuccess) {
                _uiState.update { it.copy(successToSignUp = true, isLoading = false) }
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

    fun checkValidCode() {
        val phoneNumber = uiState.value.phoneNumber
        val authenticCode = uiState.value.authenticCode
        val range = (1000..9999) // 1000 ~ 9999까지의 숫자의 범위를 지정한다.
        val randomNumber = range.random().toString()
        viewModelScope.launch {
            val result = authRepository.checkAuthenticCode(
                phoneNumber = phoneNumber,
                authenticCode = randomNumber
            )
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        authenticCode = authenticCode,
                        randomNumber = randomNumber
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        userMessage = result.exceptionOrNull()!!.localizedMessage
                    )
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

}