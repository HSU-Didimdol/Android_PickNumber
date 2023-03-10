package com.example.picknumberproject.view.login

import android.os.Bundle
import android.view.LayoutInflater
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivityLoginBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ViewBindingActivity<ActivityLoginBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}