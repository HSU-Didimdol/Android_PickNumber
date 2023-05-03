package com.example.picknumberproject.view.main.search

import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.SearchItemBinding
import com.example.picknumberproject.domain.model.CompanyEntity

class SearchViewHolder(
    private val binding: SearchItemBinding,
    private val onClickSearchItem: (CompanyEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(company: CompanyEntity) = with(binding) {
        val context = root.context
        nameTextView.text = context.getString(R.string.company_name, company.name)
        addressTextView.text = company.address
        distanceTextView.text = context.getString(R.string.distanceText, company.distance)

        searchItem.setOnClickListener {
            onClickSearchItem(company)
        }
    }

}