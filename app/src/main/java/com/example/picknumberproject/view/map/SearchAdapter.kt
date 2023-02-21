package com.example.picknumberproject.view.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.SearchItemBinding
import com.example.picknumberproject.domain.model.BankEntity


class SearchAdapter : RecyclerView.Adapter<SearchItemViewHolder>() {

    private var searchResultList: List<BankEntity> = listOf()
    lateinit var searchResultClickListener: (BankEntity) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val view = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchItemViewHolder(view, searchResultClickListener)
    }

    override fun getItemViewType(position: Int): Int = R.layout.search_item

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bindData(searchResultList[position])
        holder.bindViews(searchResultList[position])
    }

    override fun getItemCount(): Int = searchResultList.size

    fun setSearchResultList(
        searchResultList: List<BankEntity>,
        searchResultClickListener: (BankEntity) -> Unit
    ) {
        this.searchResultList = searchResultList
        this.searchResultClickListener = searchResultClickListener
        notifyDataSetChanged()
    }
}
