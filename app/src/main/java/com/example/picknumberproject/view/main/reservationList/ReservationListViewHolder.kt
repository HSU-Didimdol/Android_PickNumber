package com.example.picknumberproject.view.main.reservationList

import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.databinding.ReservationListItemBinding

class ReservationListViewHolder(
    private val binding: ReservationListItemBinding,
    private val onClickFindRoadButton: (ReservationItemUiState) -> Unit,
    private val onClickReservationPageButton: (ReservationItemUiState) -> Unit,
    private val onClickDeleteReservationButton: (ReservationItemUiState) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: ReservationItemUiState) = with(binding) {

        companyName.text = uiState.companyName
        reservationDate.text = uiState.registrationDate
        reservationNumber.text = uiState.companyNumber

        reservationButton.setOnClickListener {
            onClickReservationPageButton(uiState)
        }

        directionButton.setOnClickListener {
            onClickFindRoadButton(uiState)
        }

        deleteButton.setOnClickListener {
            onClickDeleteReservationButton(uiState)
        }
    }
}