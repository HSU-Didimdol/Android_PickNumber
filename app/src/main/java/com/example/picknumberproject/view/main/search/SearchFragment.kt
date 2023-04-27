package com.example.picknumberproject.view.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.picknumberproject.databinding.FragmentSearchBinding
import com.example.picknumberproject.view.common.ViewBindingFragment
import com.example.picknumberproject.view.main.MainActivity
import com.example.picknumberproject.view.main.reservationList.ReservationItemUiState
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch

class SearchFragment(
    private val query: String
) : ViewBindingFragment<FragmentSearchBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate

    private val mainActivity: MainActivity
        get() = activity as MainActivity

    private val viewModel: SearchViewModel by activityViewModels()


    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SearchAdapter(
            onClickItem = ::onClickSearchItem
        )

        initRecyclerView(adapter)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it, adapter)
                }
            }

        }

    }

    private fun initRecyclerView(adapter: SearchAdapter) {
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

    }

    private fun updateUi(uiState: SearchUiState, adapter: SearchAdapter) {

        if (uiState.userMessage != null) {
            viewModel.userMessageShown()
            showSnackBar(getString(uiState.userMessage))
        }
    }

    private fun onClickSearchItem(uiState: ReservationItemUiState) {

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

}