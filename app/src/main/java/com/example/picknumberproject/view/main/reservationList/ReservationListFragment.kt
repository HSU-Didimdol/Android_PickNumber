package com.example.picknumberproject.view.main.reservationList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.picknumberproject.databinding.FragmentReservationListBinding
import com.example.picknumberproject.view.common.ViewBindingFragment
import kotlinx.android.synthetic.main.reservation_list_item.*

class ReservationListFragment : ViewBindingFragment<FragmentReservationListBinding>() {

    private val viewModel: ReservationListViewModel by activityViewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReservationListBinding
        get() = FragmentReservationListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deleteButton

    }


}