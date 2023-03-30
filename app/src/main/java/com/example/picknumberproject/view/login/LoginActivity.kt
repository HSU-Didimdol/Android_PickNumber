package com.example.picknumberproject.view.login

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivityLoginBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.view.main.MainActivity
import com.example.picknumberproject.view.signup.SignUpActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ViewBindingActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEventListeners()
        phoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        viewModel.bind()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
            }
        }
    }

    private fun navigateSignUpActivity() {
        val intent = SignUpActivity.getIntent(this, "signUp")
        startActivity(intent)
    }

    private fun navigateMainActivity() {
        val intent = MainActivity.getIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        }
        startActivity(intent)
        finish()
    }

    private fun initEventListeners() {
        name.addTextChangedListener {
            if (it != null) {
                viewModel.updateUserName(it.toString())
            }
        }
        phoneNumber.addTextChangedListener {
            if (it != null) {
                viewModel.updatePhoneNumber(it.toString())
            }
        }

        // 로그인 버튼 클릭
        signIn_btn.setOnClickListener {
            viewModel.signIn()
        }

        // 회원가입 버튼 클릭
        signUp_btn.setOnClickListener {
            navigateSignUpActivity()
        }
    }

    private fun updateUi(uiState: LoginUiState) {

        nameInputLayout.apply {
            isErrorEnabled = uiState.showNameError
            error = if (uiState.showNameError) {
                context.getString(R.string.name_is_not_valid)
            } else null
        }

        phoneNumberInputLayout.apply {
            isErrorEnabled = uiState.showPhoneNumberError
            error = if (uiState.showPhoneNumberError) {
                context.getString(R.string.phone_is_not_valid)
            } else null
        }

        if (uiState.successToSignIn) {
            if (viewModel.checkUserInfoExists(uiState.phoneNumber))
                navigateMainActivity()
        }

        if (uiState.userMessage != null) {
            showSnackBar(getString(uiState.userMessage))
            viewModel.userMessageShown()
        }

        signIn_btn.apply {
            isEnabled = uiState.isInputValid && !uiState.isLoading
            setText(if (uiState.isLoading) R.string.loading else R.string.login)
        }
    }


    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}