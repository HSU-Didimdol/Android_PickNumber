package com.example.picknumberproject.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.R
import com.example.picknumberproject.domain.repository.AuthRepository
import com.example.picknumberproject.domain.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val companyRepository: CompanyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loggedIn()
    }

    fun updateUserName(userName: String) {
        _uiState.update { it.copy(userName = userName) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun bind() {
        viewModelScope.launch(Dispatchers.Main) {
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

    private fun loggedIn() {
        viewModelScope.launch {
            val result = authRepository.checkLoggedIn()
            Log.d("checkLoggedIn", result.toString())
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(isLoggedIn = result.getOrNull()!!)
                }
            } else {
                _uiState.update {
                    it.copy(
                        userMessage = R.string.login_fail,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun signIn() {
        val name = uiState.value.userName
        val password = uiState.value.password
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = authRepository.signIn(name, password)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        successToSignIn = true,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        userMessage = R.string.notSignUp_user,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun checkUserInfoExists(password: String): Boolean {
        return authRepository.hasUserInfo(password)
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}