package com.example.picknumberproject.view.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.picknumberproject.databinding.FragmentReservationBinding
import com.example.picknumberproject.view.common.ViewBindingFragment

class ReservationFragment(
    private val url: String
) : ViewBindingFragment<FragmentReservationBinding>() {


    private val viewModel: ReservationViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReservationBinding
        get() = FragmentReservationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}