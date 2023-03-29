package com.example.picknumberproject.view.main.reservationList

import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.databinding.ReservationListItemBinding

class ReservationListViewHolder(
    private val binding: ReservationListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: ReservationItemUiState) = with(binding) {

        companyName.text = uiState.companyName
        reservationDate.text = uiState.date
        reservationNumber.text = uiState.phoneNumber

        reservationButton.setOnClickListener {

        }

        directionButton.setOnClickListener {

        }

        deleteButton.setOnClickListener {

        }


    }
}