package com.example.picknumberproject.view.map

import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.databinding.SearchItemBinding
import com.example.picknumberproject.domain.model.BankEntity

class SearchItemViewHolder(
    private val binding: SearchItemBinding,
    val searchItemClickListener: (BankEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(data: BankEntity) = with(binding) {
        nameTextView.text = data.name
        addressTextView.text = data.address
        distanceTextView.text = "${data.distance} Km"
    }

    fun bindViews(data: BankEntity) {
        binding.root.setOnClickListener {
            searchItemClickListener(data)
        }
    }
}