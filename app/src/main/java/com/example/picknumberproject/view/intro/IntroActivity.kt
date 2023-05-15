package com.example.picknumberproject.view.intro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.picknumberproject.databinding.ActivityIntroBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.view.login.LoginActivity
import com.example.picknumberproject.view.login.LoginUiState
import com.example.picknumberproject.view.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroActivity : ViewBindingActivity<ActivityIntroBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ActivityIntroBinding
        get() = ActivityIntroBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.bind()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            navigateLoginActivity()
        }, 3000)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
            }
        }
    }

    private fun navigateLoginActivity() {
        val intent = LoginActivity.getIntent(this)
        startActivity(intent)
        finish()
    }

    private fun updateUi(uiState: LoginUiState) {
        if (uiState.userMessage != null) {
            viewModel.userMessageShown()
        }
    }
}