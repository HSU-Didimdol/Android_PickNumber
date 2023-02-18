package com.example.picknumberproject.view.map

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.FragmentMapBinding
import com.example.picknumberproject.domain.model.BankEntity
import com.example.picknumberproject.view.MainActivity
import com.example.picknumberproject.view.common.ViewBindingFragment
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

class MapFragment : ViewBindingFragment<FragmentMapBinding>(), OnMapReadyCallback,
    Overlay.OnClickListener {

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 Activity로 형변환하여 할당
        mainActivity = context as MainActivity
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMapBinding
        get() = FragmentMapBinding::inflate

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private val viewModel: MapViewModel by viewModels()
    //  "x": "126.9050532",
    //  "y": "37.4652659",

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it)
                }
            }
        }
    }

    override fun onMapReady(Map: NaverMap) {
        naverMap = Map

        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = true

        locationSource =
            FusedLocationSource(this@MapFragment, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        val cameraUpdate = CameraUpdate
            .scrollAndZoomTo(
                LatLng(
                    naverMap.cameraPosition.target.latitude,
                    naverMap.cameraPosition.target.longitude
                ),
                9.0
            )

        naverMap.moveCamera(cameraUpdate)
    }

    private fun updateUi(uiState: MapUiState) {
        updateMarker(uiState.bankListData)
    }

    private fun updateMarker(banks: List<BankEntity>) {
        banks.forEach { bank ->
            val marker = Marker()
            marker.position = LatLng(bank.latitude, bank.longitude)
            marker.infoWindow
            marker.map = naverMap
            marker.icon = MarkerIcons.GREEN
            marker.width = Marker.SIZE_AUTO
            marker.height = Marker.SIZE_AUTO
            marker.iconTintColor = Color.BLUE
            marker.tag =
                bank.name + "/" + bank.address + "/" + bank.distance + "/" + bank.duration + "/" + bank.code + "/" + bank.divisionCode + "/" + bank.tel
            marker.onClickListener = this


            marker.captionText = bank.name
            marker.captionTextSize = 16f
            marker.isHideCollidedSymbols = true
            marker.isHideCollidedMarkers = true

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
                Toast.makeText(mainActivity, "홈 버튼 클릭", Toast.LENGTH_SHORT).show()
                //https://www.kfcc.co.kr/map/view.do?gmgoCd={0}&name=&gmgoNm=&divCd={1}&code1={0}&code2={1}&tab=sub_tab_map
                //{0} = code, {1} = divisionCode
                val url =
                    "https://www.kfcc.co.kr/map/view.do?gmgoCd=" + bankData[4] + "&name=&gmgoNm=&divCd=00" + bankData[5] + "&code1=" + bankData[4] + "&code2=00" + bankData[5] + "&tab=sub_tab_map"
                navigationToHome(url)
            }

            callButton.setOnClickListener {
                Toast.makeText(mainActivity, "전화 버튼 클릭", Toast.LENGTH_SHORT).show()
                val call = bankData[6]
                val intent2 = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$call"))
                startActivity(intent2)

            }
            return true
        }
        return false
    }

    private fun navigationToHome(url: String) {
        val bundle = bundleOf("url" to url)
        findNavController().navigate(R.id.action_mapFragment_to_homeFragment, bundle)
    }

}