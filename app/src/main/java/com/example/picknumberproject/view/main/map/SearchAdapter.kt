package com.example.picknumberproject.view.main.map

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cursoradapter.widget.CursorAdapter
import com.example.picknumberproject.R
import kotlinx.android.synthetic.main.search_item.view.*


class SearchAdapter(context: Context) : CursorAdapter(context, null, 0) {

    private val inflater by lazy {
        LayoutInflater.from(context)
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View =
        inflater.inflate(R.layout.search_item, parent, false)

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
        val distance = cursor.getInt(cursor.getColumnIndexOrThrow("distance"))
        val nameText = view.nameTextView
        val addressText = view.addressTextView
        val distanceText = view.distanceTextView

        nameText.text = name
        addressText.text = address
        distanceText.text = distance.toString()

        val icon = view.findViewById<ImageView>(R.id.profileImage)
        icon.setImageResource(R.drawable.ic_baseline_location_on_24)
        icon.visibility = View.VISIBLE
    }

}

