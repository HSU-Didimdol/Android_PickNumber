package com.example.picknumberproject.view.main

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.picknumberproject.R

class SearchAdapter(context: Context, cursor: Cursor?) :
    CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.search_item, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val suggestionText = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val text = if (suggestionText >= 0) cursor.getString(suggestionText) else ""
        val suggestionTextView: TextView = view.findViewById(R.id.search_item)
        suggestionTextView.text = text
    }
}