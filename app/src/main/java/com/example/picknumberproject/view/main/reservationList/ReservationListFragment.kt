package com.example.picknumberproject.view.main.reservationList

import android.content.ContentValues.TAG
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_reservation_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReservationListFragment : ViewBindingFragment<FragmentReservationListBinding>() {


    private val viewModel: ReservationListViewModel by activityViewModels()
    private val initData: MutableList<ReservationItemUiState>? = null
    private var launcher: ActivityResultLauncher<Intent>? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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
        initLocationClient()

        setFragmentResultListener("refreshReservation") { _, _ ->
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                delay(300)
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
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
            adapter.notifyDataSetChanged()
        }

        launcher = registerForActivityResult(RefreshStateContract()) {
            if (it != null) {
                adapter.notifyDataSetChanged()
                it.message?.let { message -> showSnackBar(message) }
            }
        }

    }

    private fun initLocationClient() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        val locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        val client = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            Log.d(TAG, "location client setting success")
        }

        task.addOnFailureListener {
            Log.d(TAG, "location client setting failure")
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
        if (uiState.userMessage != null) {
            viewModel.userMessageShown()
            showSnackBar(getString(uiState.userMessage))
        }
    }

    private fun onClickFindRoadButton(uiState: ReservationItemUiState) {

        //자동차 길찾기
        val url = ""
        //"nmap://route/car?slat=" + uiState.latitude + "&slng=" + uiState.longitude + "&sname=" + "&dlat=" + bankData[7] + "&dlng=" + bankData[8] + "&dname=" + bankData[0] + "&appname=com.example.picknumberproject"

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
            //requireContext().startActivity(intent)
        }
    }

    private fun onClickReservationPageButton(uiState: ReservationItemUiState) {
        val reservationPageFragment =
            ReservationPageFragment("http://service.landpick.net/reservation?$uiState") // <- TODO : 임의적인 데이터
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