package com.example.picknumberproject.view.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.picknumberproject.data.dao.BankDao
import com.example.picknumberproject.data.db.BankDatabase
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.databinding.ActivitySearchBinding
import com.example.picknumberproject.domain.model.BankEntity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.*
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

    private val databaseDao: BankDao by lazy {
        BankDatabase.getDatabase(this).getBankDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()

        initAdapter()
        initViews()
        bindViews()
        initData()
    }

    private fun setData() {
        val dataList: List<BankEntity> = databaseDao.getAll()
        dataList.sortedBy(BankEntity::distance)
        Log.d("dataList", dataList.toString())
        adapter.setSearchResultList(dataList) {
            //TODO : 아이템 클릭시 UI 변화
        }
    }

    private fun searchKeyWord(keyWord: String) {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.Main) {
                    setData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@SearchActivity,
                    "검색하는 과정에서 에러가 발생했습니다. : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initAdapter() {
        adapter = SearchRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        load_state.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyWord(queryInput.text.toString())
        }
    }

    private fun initData() {
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}