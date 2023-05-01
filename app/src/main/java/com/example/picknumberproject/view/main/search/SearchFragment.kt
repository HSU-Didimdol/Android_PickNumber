package com.example.picknumberproject.view.main.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.picknumberproject.databinding.FragmentSearchBinding
import com.example.picknumberproject.domain.model.CompanyEntity
import com.example.picknumberproject.view.common.ViewBindingFragment
import com.example.picknumberproject.view.extension.RefreshStateContract
import com.example.picknumberproject.view.main.MainActivity
import com.example.picknumberproject.view.main.map.MapUiState
import com.example.picknumberproject.view.main.map.MapViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SearchFragment(
    private val query: String
) : ViewBindingFragment<FragmentSearchBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate

    private val mainActivity: MainActivity
        get() = activity as MainActivity

    private val viewModel: MapViewModel by activityViewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchAdapter(
            onClickSearchItem = ::onClickSearchItem
        )

        viewModel.bind(
            query,
            ""
        )

        initRecyclerView(adapter)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it, adapter)
                }
            }
        }

        launcher = registerForActivityResult(RefreshStateContract()) {
            if (it != null) {
                adapter.submitList(viewModel.uiState.value.companyListData)
                it.message?.let { message -> showSnackBar(message) }
            }
        }
    }

    private fun initRecyclerView(adapter: SearchAdapter) {
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun updateUi(uiState: MapUiState, adapter: SearchAdapter) {
        uiState.companyListData.sortedBy {
            it.distance.toInt()
        }
        adapter.submitList(uiState.companyListData)
        if (uiState.userMessage != null) {
            viewModel.userMessageShown()
            showSnackBar(getString(uiState.userMessage))
        }
    }

    private fun onClickSearchItem(uiState: CompanyEntity) {

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

}