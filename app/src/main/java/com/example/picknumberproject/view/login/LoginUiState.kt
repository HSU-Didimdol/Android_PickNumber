package com.example.picknumberproject.view.login

data class LoginUiState(
    val userName: String = "",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val successToSignIn: Boolean = false,
    val userMessage: Int? = null
) {
    val isInputValid: Boolean
        get() = isNameValid && isPhoneNumberValid

    private val isNameValid: Boolean
        get() = userName.length >= 3

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
}