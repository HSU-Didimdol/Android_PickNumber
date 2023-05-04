package com.example.picknumberproject.view.main.map

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.FragmentMapBinding
import com.example.picknumberproject.domain.model.CompanyEntity
import com.example.picknumberproject.view.common.ViewBindingFragment
import com.example.picknumberproject.view.main.MainActivity
import com.example.picknumberproject.view.main.homepage.HomePageFragment
import com.example.picknumberproject.view.main.reservationpage.ReservationPageFragment
import com.example.picknumberproject.view.main.search.SearchUiState
import com.example.picknumberproject.view.main.search.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.launch

class MapFragment() : ViewBindingFragment<FragmentMapBinding>(), OnMapReadyCallback,
    Overlay.OnClickListener {

    private lateinit var map: NaverMap
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    private val mainActivity: MainActivity
        get() = activity as MainActivity

    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        routeButton.isVisible = false
    }

    override fun onMapReady(Map: NaverMap) {
        map = Map
        val uiSetting = map.uiSettings
        uiSetting.isLocationButtonEnabled = true
        map.locationSource = locationSource

        if (!viewModel.notValidCurrentState()) {
            viewModel.uiState.value.currentState
            onClick(viewModel.uiState.value.currentState!!)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it)
                }
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
            if (!locationSource.isActivated) {
                map.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
    }

    private fun updateUi(uiState: SearchUiState) {
        updateMarker(uiState)
        if (uiState.userMessage != null) {
            showSnackBar(getString(uiState.userMessage))
            viewModel.userMessageShown()
        }
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun updateMarker(uiState: SearchUiState) {
        val companyList = uiState.companyListData
        Log.d("company", companyList.toString())

        // 마커들의 위치 정보를 기반으로 중심점과 확대/축소 레벨 계산
        var totalLat = 0.0
        var totalLng = 0.0

        companyList.forEach { company ->
            val marker = Marker()
            marker.position = LatLng(company.latitude.toDouble(), company.longitude.toDouble())
            marker.infoWindow
            marker.map = map
            marker.icon = MarkerIcons.GREEN
            marker.width = Marker.SIZE_AUTO
            marker.height = Marker.SIZE_AUTO
            marker.iconTintColor = Color.MAGENTA
            marker.captionText = company.name + " " + company.divisionName
            marker.captionTextSize = 16f
            marker.onClickListener = this
            marker.tag =
                "새마을 금고 본점(" + company.name + ")" + "/" + company.address + "/" + company.distance + "/" + company.duration + "/" + company.code + "/" + company.divisionCode + "/" + company.tel + "/" + company.latitude + "/" + company.longitude + "/" + company.companyID

            totalLat += marker.position.latitude
            totalLng += marker.position.longitude

        }

        if (viewModel.uiState.value.currentCameraLatitude != null && viewModel.uiState.value.currentCameraLongitude != null) {
            val cameraPosition = LatLng(
                viewModel.uiState.value.currentCameraLatitude!!,
                viewModel.uiState.value.currentCameraLongitude!!
            )
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(cameraPosition, 12.0)
            map.moveCamera(cameraUpdate)
        } else {
            val centerLat = totalLat / companyList.size
            val centerLng = totalLng / companyList.size

            val center = LatLng(centerLat, centerLng)
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(center, 12.0)

            map.moveCamera(cameraUpdate)
        }
    }

    override fun onClick(p0: Overlay): Boolean {
        routeButton.isVisible = true


        if (p0 is Marker) {
            val companyData = p0.tag.toString().split("/")
            val location = LatLng(companyData[7].toDouble(), companyData[8].toDouble())
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(location, 12.0)
            map.moveCamera(cameraUpdate)
            bottomSheetNameTextView.text = companyData[0]
            bottomSheetNameTextView.setOnClickListener {
                val clickLocation = LatLng(companyData[7].toDouble(), companyData[8].toDouble())
                val clickCameraUpdate = CameraUpdate.scrollAndZoomTo(clickLocation, 12.0)
                map.moveCamera(clickCameraUpdate)
            }
            bottomSheetAddressTextView.text = companyData[1]
            bottomSheetDistanceTextView.text = getString(R.string.Km, companyData[2])

            // 소요시간 '시간 분' 으로 맞추기
            val duration = companyData[3].toInt()

            if (duration >= 60) {
                val hour = duration / 60
                val minute = duration % 60
                bottomSheetDurationTextView.text =
                    getString(R.string.HourMin, hour.toString(), minute.toString())
            } else {
                bottomSheetDurationTextView.text = getString(R.string.Duration, duration.toString())
            }

            homeButton.setOnClickListener {
                viewModel.updateCurrentLanLat(
                    latitude = map.cameraPosition.target.latitude,
                    longitude = map.cameraPosition.target.longitude,
                    tag = p0
                )
                Toast.makeText(context, "홈 버튼 클릭", Toast.LENGTH_SHORT).show()
                val url =
                    "https://www.kfcc.co.kr/map/view.do?gmgoCd=" + companyData[4] + "&name=&gmgoNm=&divCd=00" + companyData[5] + "&code1=" + companyData[4] + "&code2=00" + companyData[5] + "&tab=sub_tab_map"
                navigationToHomePage(url)
            }

            reservationButton.setOnClickListener {
                viewModel.updateCurrentLanLat(
                    latitude = map.cameraPosition.target.latitude,
                    longitude = map.cameraPosition.target.longitude,
                    tag = p0
                )
                Toast.makeText(context, "예약 버튼 클릭", Toast.LENGTH_SHORT).show()
                val url = "http://service.landpick.net/reservation?${companyData[9]}"
                navigationToReservation(url)
            }

            callButton.setOnClickListener {
                viewModel.updateCurrentLanLat(
                    latitude = map.cameraPosition.target.latitude,
                    longitude = map.cameraPosition.target.longitude,
                    tag = p0
                )
                Toast.makeText(context, "전화 버튼 클릭", Toast.LENGTH_SHORT).show()
                val call = companyData[6]
                val intent2 = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$call"))
                startActivity(intent2)
            }

            routeButton.setOnClickListener {
                viewModel.updateCurrentLanLat(
                    latitude = map.cameraPosition.target.latitude,
                    longitude = map.cameraPosition.target.longitude,
                    tag = p0
                )
                //자동차 길찾기
                val url =
                    "nmap://route/car?slat=" + map.cameraPosition.target.latitude + "&slng=" + map.cameraPosition.target.longitude + "&sname=" + "&dlat=" + companyData[7] + "&dlng=" + companyData[8] + "&dname=" + companyData[0] + "&appname=com.example.picknumberproject"

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
            return true
        }
        return false
    }

    private fun navigationToHomePage(url: String) {
        val homePageFragment = HomePageFragment(url = url)
        mainActivity.replaceFragment(homePageFragment)
    }

    private fun navigationToReservation(url: String) {
        val reservationPageFragment = ReservationPageFragment(url = url)
        mainActivity.replaceFragment(reservationPageFragment)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}