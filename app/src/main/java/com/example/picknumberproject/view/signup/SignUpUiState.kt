package com.example.picknumberproject.view.signup

data class SignUpUiState(
    val userName: String = "",
    val phoneNumber: String = "",
    val authenticCode: String = "",
    val randomNumber: String = "0000",
    val isLoading: Boolean = false,
    val checkBox: Boolean = false,
    val successToSignUp: Boolean = false,
    val userMessage: String? = null
) {
    val isInputValid: Boolean
        get() = isNameValid && isPhoneNumberValid && isCodeValid

    private val isCodeValid: Boolean
        get() = authenticCode == randomNumber

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

    val showAuthenticCode: Boolean
        get() = authenticCode.isNotEmpty() && !isCodeValid
}