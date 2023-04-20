package com.example.picknumberproject.view.signup

import com.example.picknumberproject.domain.model.UserEntity

data class SignUpUiState(
    val userName: String = "",
    val userPassword: String = "",
    val confirmPassword: String = "",
    val phoneNumber: String = "",
    val authenticCode: String = "",
    val randomNumber: String = "0000",
    val isLoading: Boolean = false,
    val checkBox: Boolean = false,
    val successToSignUp: Boolean = false,
    val successToLogOut: Boolean = false,
    val userMessage: Int? = null,
    val userEntity: UserEntity? = null
) {
    val isInputValid: Boolean
        get() = isNameValid && isPhoneNumberValid && isCodeValid && isPasswordValid

    private val isCodeValid: Boolean
        get() = authenticCode == randomNumber

    private val isNameValid: Boolean
        get() = userName.length >= 3

    private val isPasswordValid: Boolean
        get() {
            /**
             *   비밀번호 조건: 최소 6자 이상, 최소한 한개의 영문자와 숫자를 포함해야 함
             */
            val passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$(?i)"
            val regex = Regex(passwordRegex)
            return userPassword.matches(regex)
        }

    private val isPhoneNumberValid: Boolean
        get() {
            return if (phoneNumber.isEmpty()) {
                false
            } else {
                android.util.Patterns.PHONE.matcher(phoneNumber).matches()
            }
        }

    val showNameError: Boolean
        get() = userName.isNotEmpty() && !isNameValid

    val showPhoneNumberError: Boolean
        get() = phoneNumber.isNotEmpty() && !isPhoneNumberValid

    val showAuthenticCode: Boolean
        get() = authenticCode.isNotEmpty() && !isCodeValid
}