package com.example.picknumberproject.view.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.picknumberproject.databinding.SearchItemBinding

class SearchAdapter(
    private val onClickItem: (SearchItemUiState) -> Unit
) : ListAdapter<SearchItemUiState, SearchViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(layoutInflater, parent, false)
        return SearchViewHolder(binding, onClickItem = onClickItem)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun submitList(list: List<SearchItemUiState>?) {
        super.submitList(list)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SearchItemUiState>() {
            override fun areItemsTheSame(
                oldItem: SearchItemUiState,
                newItem: SearchItemUiState
            ): Boolean {
                return oldItem.companyID == newItem.companyID
            }

            override fun areContentsTheSame(
                oldItem: SearchItemUiState,
                newItem: SearchItemUiState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}