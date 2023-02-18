package com.example.picknumberproject.view

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.picknumberproject.R
import com.example.picknumberproject.data.db.BankDao
import com.example.picknumberproject.data.db.BankDatabase
import com.example.picknumberproject.databinding.ActivityMainBinding
import com.example.picknumberproject.domain.model.BankEntity
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : ViewBindingActivity<ActivityMainBinding>(), OnMapReadyCallback,
    Overlay.OnClickListener {

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val mapView: MapView by lazy {
        findViewById(R.id.mapView)
    }

    private val viewModel: MainViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private val databaseDao: BankDao by lazy {
        BankDatabase.getDatabase(this).getBankDao()
    }

    //  "x": "126.9050532",
    //  "y": "37.4652659",

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
            FusedLocationSource(this@MainActivity, LOCATION_PERMISSION_REQUEST_CODE)
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

    private fun updateUi(uiState: MainUiState) {
        updateMarker(uiState.bankListData)
    }

    private fun updateMarker(banks: List<BankEntity>) {
        banks.forEach { bank ->
            val marker = Marker()
            marker.position = LatLng(bank.latitude, bank.longitude)
            marker.infoWindow
            marker.map = naverMap
            marker.icon = MarkerIcons.GREEN
            //marker.icon = OverlayImage.fromResource(R.drawable.bankmarker)
            marker.width = Marker.SIZE_AUTO
            marker.height = Marker.SIZE_AUTO
            marker.iconTintColor = Color.BLUE
            marker.tag =
                bank.name + "/" + bank.address + "/" + bank.distance + "/" + bank.duration + "/" + bank.code + "/" + bank.tel
            marker.onClickListener = this


            marker.captionText = bank.name
            marker.captionTextSize = 16f

            val infoWindow = InfoWindow()
            infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return bank.name
                }
            }
            //infoWindow.open(marker)
            marker.isHideCollidedSymbols = true
            marker.isHideCollidedMarkers = true


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
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
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
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

            reservationButton.setOnClickListener { overlay ->
                Toast.makeText(this, "예약 버튼 클릭", Toast.LENGTH_SHORT).show()
                //https://www.kfcc.co.kr/map/view.do?gmgoCd={0}&name=&gmgoNm=&divCd={1}&code1={0}&code2={1}&tab=sub_tab_map
                //{0} = code, {1} = divisionCode
                var url =
                    "https://www.kfcc.co.kr/map/view.do?gmgoCd=" + bankData[4] + "&name=&gmgoNm=&divCd=00" + bankData[5] + "&code1=" + bankData[4] + "&code2=00" + bankData[5] + "&tab=sub_tab_map"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                true
            }

            callButton.setOnClickListener { overlay ->
                Toast.makeText(this, "전화 버튼 클릭", Toast.LENGTH_SHORT).show()
                var call = bankData[6]
                val intent2 = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + call))
                startActivity(intent2)

            }
            return true
        }
        return false
    }
}