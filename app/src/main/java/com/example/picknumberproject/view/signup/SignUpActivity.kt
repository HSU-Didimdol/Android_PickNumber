package com.example.picknumberproject.view.signup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
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
        fun getIntent(context: Context, signUpOrInfoUpdate: String): Intent {
            return Intent(context, SignUpActivity::class.java).apply {
                putExtra("signUpOrInfoUpdate", signUpOrInfoUpdate)
            }
        }
    }

    private fun getSignUpOrInfoUpdate(): String {
        return intent.getStringExtra("signUpOrInfoUpdate")!!
    }

    private val viewModel: SignUpViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ActivitySignUpBinding
        get() = ActivitySignUpBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEventListeners()
        userPhoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        setSupportActionBar(signup_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //타이틀 안보이게
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //왼쪽 뒤로가기 사용여부
        supportActionBar?.setHomeAsUpIndicator(R.drawable.cancel_button) //왼쪽 뒤로가기 아이콘 변경

        val getSignUpOrInfoUpdate = getSignUpOrInfoUpdate()

        if (getSignUpOrInfoUpdate == "infoUpdate") {
            signup_toolbar_title.text = "정보 수정"
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    @SuppressLint("ResourceAsColor")
    private fun initEventListeners() {
        userNameEditText.addTextChangedListener {
            if (it != null) {
                viewModel.updateUserName(it.toString())
            }
        }

        userPasswordEditText.addTextChangedListener {
            if (it != null) {
                viewModel.updateUserPassword(it.toString())
            }
        }


        confirmPasswordEditText.addTextChangedListener {
            if (it != null) {
                viewModel.updateConfirmPassword(it.toString())
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

        if (agreementCheckBox.isChecked) {
            viewModel.updateCheckBox(true)
        }


        verificationCodeButton.setOnClickListener {
            if (userPhoneEditText.length() == 0) {
                showSnackBar("전화번호를 입력해주세요.")
            } else { // 전화번호를 입력하고 요청 버튼 누르게
                Log.d("요청 버튼 눌림", userPhoneEditText.text.toString())
                viewModel.checkValidCode()
                verificationCodeButton.isEnabled = false // 버튼 비활성화
                verificationCodeButton.setBackgroundColor(R.color.gray)
                setTimer()
            }
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
            } else {
                signupButton.setOnClickListener {
                    cancel()
                    viewModel.signUp()
                }
            }
            if (second == 0) {
                minute--
                second = 60
            }
            second--
        }
    }

}
