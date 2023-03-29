package com.example.picknumberproject.view.main.reservationList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.picknumberproject.databinding.ReservationListItemBinding

class ReservationListAdapter() :
    ListAdapter<ReservationItemUiState, ReservationListViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ReservationListItemBinding.inflate(layoutInflater, parent, false)
        return ReservationListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationListViewHolder, position: Int) {
        holder.bind(currentList[position])
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