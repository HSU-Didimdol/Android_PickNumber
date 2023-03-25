package com.example.picknumberproject.view.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivityInfoUpdateBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_info_update.*

@AndroidEntryPoint
class InfoUpdateActivity : ViewBindingActivity<ActivityInfoUpdateBinding>() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, InfoUpdateActivity::class.java)
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivityInfoUpdateBinding
        get() = ActivityInfoUpdateBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_update)

        setSupportActionBar(info_update_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //타이틀 안보이게
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //왼쪽 뒤로가기 사용여부
        supportActionBar?.setHomeAsUpIndicator(R.drawable.cancel_button) //왼쪽 뒤로가기 아이콘 변경

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.menu_infoupdate,
            menu
        )
        return true
    }
}