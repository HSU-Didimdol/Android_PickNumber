package com.example.picknumberproject.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.example.picknumber_androidproject.common.ViewBindingActivity
import com.example.picknumberproject.R
import com.example.picknumberproject.api.RetrofitUtil
import com.example.picknumberproject.databinding.ActivityMapBinding
import com.example.picknumberproject.model.BankEntity
import com.example.picknumberproject.model.toEntity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MapActivity : ViewBindingActivity<ActivityMapBinding>(), OnMapReadyCallback,
    CoroutineScope {

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private lateinit var dataList: List<BankEntity>

    private val mapView: MapView by lazy {
        findViewById(R.id.mapView)
    }
    override val bindingInflater: (LayoutInflater) -> ActivityMapBinding
        get() = ActivityMapBinding::inflate

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    //  "x": "126.9050532",
    //  "y": "37.4652659",

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        job = Job()

        WindowCompat.setDecorFitsSystemWindows(window, true)

        binding.searchButton.setOnClickListener {
            startSearchActivity()
        }
        getBankList()
    }

    override fun onMapReady(Map: NaverMap) {
        naverMap = Map

        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = true

        locationSource =
            FusedLocationSource(this@MapActivity, LOCATION_PERMISSION_REQUEST_CODE)
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

    private fun getBankList() {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.bankApi.getBankList()
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            dataList = it.map { bank ->
                                bank.toEntity()
                            }
                            dataList = getBankDistance(dataList)
                            Log.d("dataList 1", dataList.toString())
                            withContext(Dispatchers.Main) {
                                updateMarker(dataList)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@MapActivity,
                    "에러가 발생했습니다. : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun getBankDistance(banksEntity: List<BankEntity>): List<BankEntity> {
        banksEntity.forEach { bankEntity ->
            val deferred: Deferred<Int> = coroutineScope {
                async {
                    val goal = "${bankEntity.longitude},${bankEntity.latitude}"
                    try {
                        val response = RetrofitUtil.direction5Api.getDistance(
                            start = "126.9050532,37.4652659",
                            goal = goal
                        )
                        if (response.isSuccessful) {
                            val body = response.body()
                            check(body != null) { "body 응답이 없습니다." }
                            return@async body.route.traoptimal[0].summary.distance
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return@async 0
                }
            }
            bankEntity.distance = deferred.await()
        }
        return banksEntity
    }

    private fun updateMarker(banks: List<BankEntity>) {
        banks.forEach { bank ->
            val marker = Marker()
            marker.position = LatLng(bank.latitude, bank.longitude)
            marker.infoWindow
            marker.map = naverMap
            marker.icon = MarkerIcons.BLACK
            marker.width = Marker.SIZE_AUTO
            marker.height = Marker.SIZE_AUTO
            marker.iconTintColor = Color.BLUE
            val infoWindow = InfoWindow()
            infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return bank.name
                }
            }
            infoWindow.open(marker)

            marker.isHideCollidedSymbols = true
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

    private fun startSearchActivity() {
        val intent = SearchActivity.getIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        }
        startActivity(intent)
    }
}