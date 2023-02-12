package com.example.picknumberproject.view.search

import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.databinding.ItemSearchBinding
import com.example.picknumberproject.domain.model.BankEntity

class SearchItemViewHolder(
    private val binding: ItemSearchBinding,
    val searchItemClickListener: (BankEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(data: BankEntity) = with(binding) {
        nameTextView.text = data.name
        addressTextView.text = data.address
        distanceTextView.text = data.distance.toString()
    }

    fun bindViews(data: BankEntity) {
        binding.root.setOnClickListener {
            searchItemClickListener(data)
        }
    }
}