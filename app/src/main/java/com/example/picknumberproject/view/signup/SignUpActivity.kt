package com.example.picknumberproject.view.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivitySignUpBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_sign_up.*

@AndroidEntryPoint
class SignUpActivity : ViewBindingActivity<ActivitySignUpBinding>() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignUpBinding
        get() = ActivitySignUpBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setSupportActionBar(signup_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //타이틀 안보이게
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //왼쪽 뒤로가기 사용여부
        supportActionBar?.setHomeAsUpIndicator(R.drawable.cancel_button) //왼쪽 뒤로가기 아이콘 변경

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.menu_signup,
            menu
        )
        return true
    }
}