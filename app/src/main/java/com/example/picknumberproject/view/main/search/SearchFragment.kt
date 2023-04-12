package com.example.picknumberproject.view.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.picknumberproject.databinding.FragmentSearchBinding
import com.example.picknumberproject.view.common.ViewBindingFragment

class SearchFragment : ViewBindingFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by activityViewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}