package com.example.picknumberproject.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.R
import com.example.picknumberproject.domain.repository.AuthRepository
import com.example.picknumberproject.domain.repository.CompanyRepository
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

    fun updateUserPassword(userPassword: String) {
        _uiState.update { it.copy(userPassword = userPassword) }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
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
        val password = uiState.value.userPassword
        val confirmPassword = uiState.value.confirmPassword
        val phoneNumber = uiState.value.phoneNumber

        if (password != confirmPassword) {
            _uiState.update { it.copy(userMessage = R.string.password_mismatch) }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authRepository.signUp(name, password, phoneNumber)
            if (result.isSuccess) {
                _uiState.update { it.copy(successToSignUp = true, isLoading = false) }
            } else {
                _uiState.update {
                    it.copy(
                        userMessage = R.string.do_not_signUp,
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
                        userMessage = R.string.not_valid_code
                    )
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

}