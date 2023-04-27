package com.example.picknumberproject.view.main.search

import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.databinding.SearchItemBinding

class SearchViewHolder(
    private val binding: SearchItemBinding,
    private val onClickItem: (SearchItemUiState) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(uiState: SearchItemUiState) = with(binding) {
        nameTextView.text
        addressTextView.text
        distanceTextView.text

        searchItem.setOnClickListener {

        }
    }

}