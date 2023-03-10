package com.example.picknumberproject.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivitySignUpBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : ViewBindingActivity<ActivitySignUpBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivitySignUpBinding
        get() = ActivitySignUpBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }
}