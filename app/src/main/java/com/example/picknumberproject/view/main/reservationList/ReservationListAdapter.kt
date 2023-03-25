package com.example.picknumberproject.view.main.reservationList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.picknumberproject.databinding.ReservationListItemBinding
import com.example.picknumberproject.domain.model.ReservationEntity

class ReservationListAdapter(
    private val onClick
) : PagingDataAdapter<ReservationEntity, ReservationListViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ReservationListItemBinding.inflate(layoutInflater, parent, false)
        return ReservationListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ReservationListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ReservationEntity>
    }

}