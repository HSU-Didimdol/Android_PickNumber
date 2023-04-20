package com.example.picknumberproject.view.main.reservationList

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ReservationListFragment : ViewBindingFragment<FragmentReservationListBinding>() {

    private val viewModel: ReservationListViewModel by activityViewModels()
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

        val adapter = ReservationListAdapter(
            onClickFindRoadButton = ::onClickFindRoadButton,
            onClickReservationPageButton = ::onClickReservationPageButton,
            onClickDeleteReservationButton = ::onClickDeleteReservationButton
        )
        initRecyclerView(adapter)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it, adapter)
                }
            }
        }

        checkButton.setOnClickListener {
            viewModel.fetchReservations()
        }

        launcher = registerForActivityResult(RefreshStateContract()) {
            if (it != null) {
                adapter.submitList(viewModel.uiState.value.reservations)
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
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
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

    @SuppressLint("SetTextI18n")
    private fun updateUi(uiState: ReservationListUiState, adapter: ReservationListAdapter) {
        binding.loadState.emptyText.isVisible =
            uiState.reservations.isEmpty() // 만약 리스트에 아무것도 없다면 "예약 없음" 텍스트가 반환된다.

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // 날짜 별로 정렬
        val sortedList = uiState.reservations.sortedBy {
            LocalDateTime.parse(it.registrationDate, formatter)
        }

        adapter.submitList(sortedList)
        Log.d("uiState.reservations.size", uiState.reservations.size.toString())
        total.text = "총 ${uiState.reservations.size} 건"
        if (uiState.userMessage != null) {
            viewModel.userMessageShown()
            showSnackBar(uiState.userMessage)
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
            ReservationPageFragment("http://service.landpick.net/reservation?${uiState.companyID}")
        mainActivity.replaceFragment(reservationPageFragment)
    }

    private fun onClickDeleteReservationButton(uiState: ReservationItemUiState) {
        viewModel.reservationDelete(uiState)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

}