package com.example.picknumberproject.view.main.map

import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.databinding.SearchItemBinding
import com.example.picknumberproject.domain.model.CompanyEntity

class SearchItemViewHolder(
    private val binding: SearchItemBinding,
    val searchItemClickListener: (CompanyEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(data: CompanyEntity) = with(binding) {
        nameTextView.text = data.name
        addressTextView.text = data.address
        distanceTextView.text = "${data.distance} Km"
    }

    fun bindViews(data: CompanyEntity) {
        binding.root.setOnClickListener {
            searchItemClickListener(data)
        }
    }
}