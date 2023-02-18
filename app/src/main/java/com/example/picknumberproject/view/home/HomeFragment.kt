package com.example.picknumberproject.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.picknumberproject.databinding.FragmentHomeBinding
import com.example.picknumberproject.view.common.ViewBindingFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : ViewBindingFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("url") ?: ""
        webView.loadUrl(url)
    }

}