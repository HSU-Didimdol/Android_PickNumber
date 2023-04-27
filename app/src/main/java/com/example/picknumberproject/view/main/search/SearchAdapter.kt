package com.example.picknumberproject.view.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.picknumberproject.databinding.SearchItemBinding
import com.example.picknumberproject.domain.model.CompanyEntity

class SearchAdapter(
    private val onClickSearchItem: (CompanyEntity) -> (Unit)
) : ListAdapter<CompanyEntity, SearchViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(layoutInflater, parent, false)
        return SearchViewHolder(binding, onClickSearchItem = onClickSearchItem)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun submitList(list: List<CompanyEntity>?) {
        super.submitList(list)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<CompanyEntity>() {
            override fun areItemsTheSame(
                oldItem: CompanyEntity,
                newItem: CompanyEntity
            ): Boolean {
                return oldItem.companyID == newItem.companyID
            }

            override fun areContentsTheSame(
                oldItem: CompanyEntity,
                newItem: CompanyEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}