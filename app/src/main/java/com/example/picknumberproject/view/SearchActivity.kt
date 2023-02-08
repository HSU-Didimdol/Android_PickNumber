package com.example.picknumberproject.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.example.picknumber_androidproject.common.ViewBindingActivity
import com.example.picknumberproject.databinding.ActivitySearchBinding

class SearchActivity : ViewBindingActivity<ActivitySearchBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivitySearchBinding
        get() = ActivitySearchBinding::inflate

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

    }
}