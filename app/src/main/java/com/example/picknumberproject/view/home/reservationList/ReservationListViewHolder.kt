package com.example.picknumberproject.view.home.reservationList

import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ReservationListItemBinding

class ReservationListViewHolder(
    private val binding: ReservationListItemBinding,
    private val onClickFindRoadButton: (ReservationItemUiState) -> Unit,
    private val onClickReservationPageButton: (ReservationItemUiState) -> Unit,
    private val onClickDeleteReservationButton: (ReservationItemUiState) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: ReservationItemUiState) = with(binding) {
        val context = root.context
        companyName.text = uiState.companyName
        reservationDate.text =
            context.getString(R.string.reservation_date_item, uiState.date, uiState.hour)
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