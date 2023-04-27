package com.example.picknumberproject.view.main.search

import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.databinding.SearchItemBinding
import com.example.picknumberproject.domain.model.CompanyEntity

class SearchViewHolder(
    private val binding: SearchItemBinding,
    private val onClickSearchItem: (SearchItemUiState) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(company: CompanyEntity) = with(binding) {
        nameTextView.text = company.name
        addressTextView.text = company.address
        distanceTextView.text = company.distance

        searchItem.setOnClickListener {

        }
    }

}