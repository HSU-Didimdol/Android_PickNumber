package com.example.picknumberproject.view.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivitySignUpBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

@AndroidEntryPoint
class SignUpActivity : ViewBindingActivity<ActivitySignUpBinding>() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

    private val viewModel: SignUpViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ActivitySignUpBinding
        get() = ActivitySignUpBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEventListeners()
        userPhoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
            }
        }

    }

    private fun navigateLoginActivity() {
        finish()
    }

    private fun updateUi(uiState: SignUpUiState) {

        // 추후에 체크 박스가 체크되면 버튼이 활성화 되게끔
        if (agreementCheckBox.isChecked) {

        }

        if (uiState.successToSignUp) {
            navigateLoginActivity()
        }

        if (uiState.userMessage != null) {
            showSnackBar(getString(uiState.userMessage))
            viewModel.userMessageShown()
        }

        signupButton.apply {
            isEnabled = uiState.isInputValid && !uiState.isLoading
            setText(if (uiState.isLoading) R.string.loading else R.string.signUp)
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun initEventListeners() {
        userNameEditText.addTextChangedListener {
            if (it != null) {
                viewModel.updateUserName(it.toString())
            }
        }

        userPhoneEditText.addTextChangedListener {
            if (it != null) {
                viewModel.updatePhoneNumber(it.toString())
            }
        }

        verificationCodeEditText.addTextChangedListener {
            if (it != null) {
                viewModel.updateAuthenticCode(it.toString())
            }
        }

        verificationCodeButton.setOnClickListener {
            viewModel.checkValidCode()
            setTimer()
        }

        signupButton.setOnClickListener {
            viewModel.signUp()
        }

    }

    private fun setTimer() {
        val time = 120
        var second = time % 60
        var minute = time / 60

        timer(period = 1000, initialDelay = 1000) {
            runOnUiThread {
                timeTextView.text = String.format("0$minute:%02d", second)
            }
            if (second == 0 && minute == 0) {
                // 타이머 종료
                Log.d("setTimer()", "타이머 종료")
                cancel()
                runOnUiThread {
                    timeTextView.text = String.format("02:00")
                    Toast.makeText(applicationContext, "인증 시간이 만료되었습니다.", Toast.LENGTH_SHORT).show()
                }
                return@timer
            }

            if (second == 0) {
                minute--
                second = 60
            }
            second--
        }
    }

}
