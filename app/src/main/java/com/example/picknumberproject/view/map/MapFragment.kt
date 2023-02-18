package com.example.picknumberproject.view.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.picknumberproject.databinding.FragmentMapBinding
import com.example.picknumberproject.view.common.ViewBindingFragment

class MapFragment : ViewBindingFragment<FragmentMapBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}