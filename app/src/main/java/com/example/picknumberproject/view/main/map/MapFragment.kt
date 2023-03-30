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
import com.example.picknumberproject.databinding.FragmentMapBinding
import com.example.picknumberproject.view.common.ViewBindingFragment
import com.example.picknumberproject.view.main.MainActivity
import com.example.picknumberproject.view.main.homepage.HomePageFragment
import com.example.picknumberproject.view.main.reservationpage.ReservationPageFragment
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

class MapFragment(
    private val query: String
) : ViewBindingFragment<FragmentMapBinding>(), OnMapReadyCallback,
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

    private val viewModel: MapViewModel by activityViewModels()
    //  "x": "126.9050532",
    //  "y": "37.4652659",

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val longitude = locationSource.lastLocation?.longitude
        val latitude = locationSource.lastLocation?.latitude


        viewModel.bind(query, "${longitude},${latitude}")

        routeButton.isVisible = false
    }

    override fun onMapReady(Map: NaverMap) {
        map = Map
        val uiSetting = map.uiSettings
        uiSetting.isLocationButtonEnabled = true
        map.locationSource = locationSource
        map.locationTrackingMode = LocationTrackingMode.Follow
        val cameraUpdate = CameraUpdate
            .scrollAndZoomTo(
                LatLng(
                    map.cameraPosition.target.latitude,
                    map.cameraPosition.target.longitude
                ),
                9.0
            )
        map.moveCamera(cameraUpdate)

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

    private fun updateUi(uiState: MapUiState) {
        updateMarker(uiState)
        if (uiState.userMessage != null) {
            showSnackBar(uiState.userMessage)
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

    private fun updateMarker(uiState: MapUiState) {
        val companyList = uiState.companyListData
        Log.d("company", companyList.toString())
        companyList.forEach { company ->
            val marker = Marker()
            marker.position = LatLng(company.latitude.toDouble(), company.longitude.toDouble())
            marker.infoWindow
            marker.map = map
            marker.icon = MarkerIcons.GREEN
            marker.width = Marker.SIZE_AUTO
            marker.height = Marker.SIZE_AUTO
            marker.iconTintColor = Color.BLUE
            marker.onClickListener = this
            marker.tag =
                "새마을 금고 본점(" + company.name + ")" + "/" + company.address + "/" + company.distance + "/" + company.duration + "/" + company.code + "/" + company.divisionCode + "/" + company.tel + "/" + company.latitude + "/" + company.longitude + "/" + company.companyID
        }
    }

    override fun onClick(p0: Overlay): Boolean {
        routeButton.isVisible = true
        if (p0 is Marker) {
            Log.d("p0:", p0.tag.toString())
            val bankData = p0.tag.toString().split("/")
            bottomSheetNameTextView.text = bankData[0]
            bottomSheetAddressTextView.text = bankData[1]
            bottomSheetDistanceTextView.text = bankData[2] + " km"

            // 소요시간 '시간 분' 으로 맞추기
            val duration = bankData[3].toInt()

            if (duration >= 60) {
                val hour = duration / 60
                val minute = duration % 60
                bottomSheetDurationTextView.text = "$hour 시간 $minute 분"
            } else {
                bottomSheetDurationTextView.text = "$duration 분"
            }

            homeButton.setOnClickListener {
                Toast.makeText(context, "홈 버튼 클릭", Toast.LENGTH_SHORT).show()
                //https://www.kfcc.co.kr/map/view.do?gmgoCd={0}&name=&gmgoNm=&divCd={1}&code1={0}&code2={1}&tab=sub_tab_map
                //{0} = code, {1} = divisionCode
                val url =
                    "https://www.kfcc.co.kr/map/view.do?gmgoCd=" + bankData[4] + "&name=&gmgoNm=&divCd=00" + bankData[5] + "&code1=" + bankData[4] + "&code2=00" + bankData[5] + "&tab=sub_tab_map"
                navigationToHomePage(url)
            }

            reservationButton.setOnClickListener {
                Toast.makeText(context, "예약 버튼 클릭", Toast.LENGTH_SHORT).show()
                val url = "http://service.landpick.net/reservation?${bankData[9]}"
                navigationToReservation(url)
            }

            callButton.setOnClickListener {
                Toast.makeText(context, "전화 버튼 클릭", Toast.LENGTH_SHORT).show()
                val call = bankData[6]
                val intent2 = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$call"))
                startActivity(intent2)

            }

            routeButton.setOnClickListener {
                //자동차 길찾기
                val url =
                    "nmap://route/car?slat=" + map.cameraPosition.target.latitude + "&slng=" + map.cameraPosition.target.longitude + "&sname=" + "&dlat=" + bankData[7] + "&dlng=" + bankData[8] + "&dname=" + bankData[0] + "&appname=com.example.picknumberproject"

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
