package com.example.picknumberproject.view.login

data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val successToSignIn: Boolean = false,
    val userMessage: Int? = null,
    val isLoggedIn: Boolean = false
) {
    val isInputValid: Boolean
        get() = isNameValid && isPasswordValid

    private val isNameValid: Boolean
        get() = userName.length >= 3

    private val isPasswordValid: Boolean
        get() {
            //비밀번호 조건: 최소 6자 이상, 최소한 한개의 영문자와 숫자를 포함해야 함
            val passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$(?i)"
            val regex = Regex(passwordRegex)
            return password.matches(regex)
        }

    val showNameError: Boolean
        get() = userName.isNotEmpty() && !isNameValid

    val showPasswordError: Boolean
        get() = password.isNotEmpty() && !isPasswordValid
}