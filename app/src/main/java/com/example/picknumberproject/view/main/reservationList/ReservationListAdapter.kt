package com.example.picknumberproject.view.main.reservationList

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.picknumberproject.databinding.ReservationListItemBinding
import kotlinx.android.synthetic.main.fragment_reservation_list.*
import kotlin.coroutines.coroutineContext

class ReservationListAdapter(
    private val onClickFindRoadButton: (ReservationItemUiState) -> Unit,
    private val onClickReservationPageButton: (ReservationItemUiState) -> Unit,
    private val onClickDeleteReservationButton: (ReservationItemUiState) -> Unit

) : ListAdapter<ReservationItemUiState, ReservationListViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ReservationListItemBinding.inflate(layoutInflater, parent, false)
        return ReservationListViewHolder(
            binding,
            onClickFindRoadButton = onClickFindRoadButton,
            onClickReservationPageButton = onClickReservationPageButton,
            onClickDeleteReservationButton = onClickDeleteReservationButton
        )
    }

    override fun onBindViewHolder(holder: ReservationListViewHolder, position: Int) {
        holder.bind(currentList[position])
        Log.d("예약 리스트", currentList.toString())
    }

    override fun submitList(list: List<ReservationItemUiState>?) {
        super.submitList(list)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ReservationItemUiState>() {
            override fun areItemsTheSame(
                oldItem: ReservationItemUiState,
                newItem: ReservationItemUiState
            ): Boolean {
                return oldItem.companyID == newItem.companyID
            }

            override fun areContentsTheSame(
                oldItem: ReservationItemUiState,
                newItem: ReservationItemUiState
            ): Boolean {
                return oldItem == newItem
            }
        }

    }
}