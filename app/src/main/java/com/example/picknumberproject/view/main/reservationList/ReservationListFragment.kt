package com.example.picknumberproject.view.main.reservationList

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.picknumberproject.databinding.FragmentReservationListBinding
import com.example.picknumberproject.view.common.ViewBindingFragment
import com.example.picknumberproject.view.extension.RefreshStateContract
import com.example.picknumberproject.view.main.MainActivity
import com.example.picknumberproject.view.main.reservationpage.ReservationPageFragment
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.fragment_reservation_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReservationListFragment : ViewBindingFragment<FragmentReservationListBinding>() {


    private val viewModel: ReservationListViewModel by activityViewModels()
    private val initData: MutableList<ReservationItemUiState>? = null
    private var launcher: ActivityResultLauncher<Intent>? = null

    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private val mainActivity: MainActivity
        get() = activity as MainActivity

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReservationListBinding
        get() = FragmentReservationListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bind(initData)

        val adapter = ReservationListAdapter(
            onClickFindRoadButton = ::onClickFindRoadButton,
            onClickReservationPageButton = ::onClickReservationPageButton,
            onClickDeleteReservationButton = ::onClickDeleteReservationButton
        )
        initRecyclerView(adapter)

        setFragmentResultListener("refreshReservation") { _, _ ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                delay(300)
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            }
        }

        total.text = "총 ${adapter.currentList.size} 건"
        Log.d("총 예약 건수", adapter.itemCount.toString())
        Log.d("예약 리스트", adapter.currentList.toString())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it, adapter)
                }
            }
        }

        checkButton.setOnClickListener {
            adapter.notifyDataSetChanged()
        }

        launcher = registerForActivityResult(RefreshStateContract()) {
            if (it != null) {
                adapter.notifyDataSetChanged()
                it.message?.let { message -> showSnackBar(message) }
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != ReservationListFragment.LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (locationSource.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        ) {
            return
        }
    }

    private fun initRecyclerView(adapter: ReservationListAdapter) {
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun updateUi(uiState: ReservationListUiState, adapter: ReservationListAdapter) {
        adapter.submitList(uiState.reservations)
        Log.d("uiState.reservations.size", uiState.reservations.size.toString())
        total.text = "총 ${uiState.reservations.size} 건"
        if (uiState.userMessage != null) {
            viewModel.userMessageShown()
            showSnackBar(getString(uiState.userMessage))
        }
    }

    private fun onClickFindRoadButton(uiState: ReservationItemUiState) {
        val url =
            "nmap://route/car?slat=" + locationSource.lastLocation?.latitude + "&slng=" + locationSource.lastLocation?.longitude + "&sname=" + "&dlat=" + uiState.latitude + "&dlng=" + uiState.longitude + "&dname=" + uiState.companyName + "&appname=com.example.picknumberproject"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        //네이버 지도 앱 설치 여부 확인
        val installed =
            requireContext().packageManager.getLaunchIntentForPackage("com.nhn.android.nmap")
        if (installed == null) {
            requireContext().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.nhn.android.nmap")
                )
            )
        } else {
            requireContext().startActivity(intent)
        }
    }

    private fun onClickReservationPageButton(uiState: ReservationItemUiState) {
        val reservationPageFragment =
            ReservationPageFragment("http://service.landpick.net/reservation?${uiState.companyID}") // <- TODO : 임의적인 데이터
        mainActivity.replaceFragment(reservationPageFragment)
    }

    private fun onClickDeleteReservationButton(uiState: ReservationItemUiState) {
        Log.d("deleteUistate", uiState.toString())
        viewModel.reservationDelete(uiState)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

}