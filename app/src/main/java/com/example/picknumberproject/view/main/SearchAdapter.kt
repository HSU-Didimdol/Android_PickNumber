package com.example.picknumberproject.view.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.picknumberproject.R
import com.example.picknumberproject.domain.model.CompanyEntity

class SearchAdapter(context: Context, resource: Int, private val objects: List<CompanyEntity>) :
    ArrayAdapter<CompanyEntity>(context, resource, objects) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.search_item, parent, false)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val addressTextView = view.findViewById<TextView>(R.id.addressTextView)
        val distanceTextView = view.findViewById<TextView>(R.id.distanceTextView)
        val item = getItem(position)
        nameTextView.text = item?.name
        addressTextView.text = item?.address
        distanceTextView.text = item?.distance
        return view
    }
}