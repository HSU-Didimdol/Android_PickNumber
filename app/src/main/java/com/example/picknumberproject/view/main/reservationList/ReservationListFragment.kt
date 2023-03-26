package com.example.picknumberproject.view.main.reservationList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.picknumberproject.databinding.FragmentReservationListBinding
import com.example.picknumberproject.view.common.ViewBindingFragment
import com.example.picknumberproject.view.extension.PagingLoadStateAdapter
import com.example.picknumberproject.view.extension.RefreshStateContract
import com.example.picknumberproject.view.extension.registerObserverForScrollToTop
import com.example.picknumberproject.view.extension.setListeners
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_reservation_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservationListFragment : ViewBindingFragment<FragmentReservationListBinding>() {

    private val viewModel: ReservationListViewModel by activityViewModels()
    private var launcher: ActivityResultLauncher<Intent>? = null
    private val initPostPagingData: PagingData<ReservationItemUiState>? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReservationListBinding
        get() = FragmentReservationListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bind(initPostPagingData)

        val adapter = ReservationListAdapter()
        initRecyclerView(adapter)

        setFragmentResultListener("refreshRervation") { _, _ ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                delay(300)
                withContext(Dispatchers.Main) {
                    adapter.refresh()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it, adapter)
                }
            }
        }

        checkButton.setOnClickListener {
            PagingLoadStateAdapter { adapter.retry() }
        }

        launcher = registerForActivityResult(RefreshStateContract()) {
            if (it != null) {
                adapter.refresh()
                it.message?.let { message -> showSnackBar(message) }
            }
        }
    }

    private fun initRecyclerView(adapter: ReservationListAdapter) {
        binding.apply {
            recyclerView.adapter = adapter.withLoadStateFooter(
                PagingLoadStateAdapter { adapter.retry() }
            )
            recyclerView.layoutManager = LinearLayoutManager(context)

            loadState.setListeners(adapter, swipeRefreshLayout)
            loadState.emptyText.textSize = 20.0f

            adapter.registerObserverForScrollToTop(recyclerView)
        }
    }

    private fun updateUi(uiState: ReservationListUiState, adapter: ReservationListAdapter) {
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.pagingData)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}