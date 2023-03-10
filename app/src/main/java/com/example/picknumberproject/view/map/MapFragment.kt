package com.example.picknumberproject.view.map

import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.picknumberproject.databinding.FragmentMapBinding
import com.example.picknumberproject.domain.model.BankEntity
import com.example.picknumberproject.view.MainActivity
import com.example.picknumberproject.view.common.ViewBindingFragment
import com.example.picknumberproject.view.extension.hideKeyboard
import com.example.picknumberproject.view.home.HomeFragment
import com.example.picknumberproject.view.reservation.ReservationFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.launch

class MapFragment : ViewBindingFragment<FragmentMapBinding>(), OnMapReadyCallback,
    Overlay.OnClickListener {

    private lateinit var map: NaverMap

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }
    private val mainActivity: MainActivity
        get() = activity as MainActivity

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

        initSearchView()
        routeButton.isVisible = false
    }

    private fun initSearchView() {

        searchView.isSubmitButtonEnabled = true
        val adapter = SearchAdapter(requireContext())
        searchView.suggestionsAdapter = adapter

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    this@MapFragment.hideKeyboard()
                    return false

                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

        searchView.setOnSuggestionListener(
            object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                override fun onSuggestionClick(position: Int): Boolean {

                    this@MapFragment.hideKeyboard()
                    return true
                }
            })
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
        updateMarker(uiState.bankListData)
    }

    private fun updateMarker(banks: List<BankEntity>) {
        banks.forEach { bank ->
            val marker = Marker()
            marker.position = LatLng(bank.latitude, bank.longitude)
            marker.infoWindow
            marker.map = map
            marker.icon = MarkerIcons.GREEN
            marker.width = Marker.SIZE_AUTO
            marker.height = Marker.SIZE_AUTO
            marker.iconTintColor = Color.BLUE
            marker.tag =
                bank.name + "/" + bank.address + "/" + bank.distance + "/" + bank.duration + "/" + bank.code + "/" + bank.divisionCode + "/" + bank.tel + "/" + bank.latitude + "/" + bank.longitude
            marker.onClickListener = this
            marker.captionTextSize = 16f
            marker.isHideCollidedSymbols = true
            marker.isHideCollidedMarkers = true

            val infoWindow = InfoWindow()
            infoWindow.position = LatLng(bank.latitude, bank.longitude)
            infoWindow.adapter = object :
                InfoWindow.DefaultTextAdapter(requireContext()) { //DefaultViewadapter? ????????? Adapter ???????
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return bank.name
                }
            }
            infoWindow.anchor = PointF(0.5f, 0.5f)
            infoWindow.open(map)
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

    override fun onClick(p0: Overlay): Boolean {
        routeButton.isVisible = true
        if (p0 is Marker) {
            Log.d("p0:", p0.tag.toString())
            val bankData = p0.tag.toString().split("/")
            bottomSheetNameTextView.text = bankData[0]
            bottomSheetAddressTextView.text = bankData[1]
            bottomSheetDistanceTextView.text = bankData[2] + " km"

            // ???????????? '?????? ???' ?????? ?????????
            val duration = bankData[3].toInt()

            if (duration >= 60) {
                val hour = duration / 60
                val minute = duration % 60
                bottomSheetDurationTextView.text = "$hour ?????? $minute ???"
            } else {
                bottomSheetDurationTextView.text = "$duration ???"
            }

            homeButton.setOnClickListener {
                Toast.makeText(context, "??? ?????? ??????", Toast.LENGTH_SHORT).show()
                //https://www.kfcc.co.kr/map/view.do?gmgoCd={0}&name=&gmgoNm=&divCd={1}&code1={0}&code2={1}&tab=sub_tab_map
                //{0} = code, {1} = divisionCode
                val url =
                    "https://www.kfcc.co.kr/map/view.do?gmgoCd=" + bankData[4] + "&name=&gmgoNm=&divCd=00" + bankData[5] + "&code1=" + bankData[4] + "&code2=00" + bankData[5] + "&tab=sub_tab_map"
                navigationToHome(url)
            }

            reservationButton.setOnClickListener {
                Toast.makeText(context, "?????? ?????? ??????", Toast.LENGTH_SHORT).show()
                val url = "" // TODO : ?????? URL ????????? ??????
                navigationToReservation(url)
            }

            callButton.setOnClickListener {
                Toast.makeText(context, "?????? ?????? ??????", Toast.LENGTH_SHORT).show()
                val call = bankData[6]
                val intent2 = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$call"))
                startActivity(intent2)

            }

            routeButton.setOnClickListener {
                //????????? ?????????
                val url =
                    "nmap://route/car?slat=" + map.cameraPosition.target.latitude + "&slng=" + map.cameraPosition.target.longitude + "&sname=" + "&dlat=" + bankData[7] + "&dlng=" + bankData[8] + "&dname=" + bankData[0] + "&appname=com.example.picknumberproject"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addCategory(Intent.CATEGORY_BROWSABLE)

                //????????? ?????? ??? ?????? ?????? ??????
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

    private fun navigationToHome(url: String) {
        val homeFragment = HomeFragment(url = url)
        mainActivity.replaceFragment(homeFragment)
    }

    private fun navigationToReservation(url: String) {
        val reservationFragment = ReservationFragment(url = url)
        mainActivity.replaceFragment(reservationFragment)
    }
}
