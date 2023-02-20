package com.example.picknumberproject.view

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivityMainBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.view.map.MapFragment


class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        initMapFragment()
    }

    private fun initMapFragment() {
        val fragmentManager = supportFragmentManager
        val mapFragment = MapFragment()
        fragmentManager.beginTransaction().apply {
            replace(R.id.container_view, mapFragment)
        }.commit()
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction
            .replace(R.id.container_view, fragment)
            .addToBackStack(null)
            .commit()
    }
}