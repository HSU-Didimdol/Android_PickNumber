package com.example.picknumberproject.view.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.databinding.ActivitySearchBinding
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class SearchActivity : ViewBindingActivity<ActivitySearchBinding>(), CoroutineScope {

    override val bindingInflater: (LayoutInflater) -> ActivitySearchBinding
        get() = ActivitySearchBinding::inflate

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var adapter: SearchRecyclerAdapter

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAdapter()
        initViews()
        bindViews()
        initData()
    }

    private fun setData() {

    }

    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        load_state.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        queryInput.isTextInputLayoutFocusedRectEnabled
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}