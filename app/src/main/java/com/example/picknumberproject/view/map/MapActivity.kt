package com.example.picknumberproject.view.map

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.example.picknumberproject.R
import com.example.picknumberproject.data.api.RetrofitUtil
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.databinding.ActivityMapBinding
import com.example.picknumberproject.data.dao.BankDao
import com.example.picknumberproject.data.db.BankDatabase
import com.example.picknumberproject.domain.model.BankEntity
import com.example.picknumberproject.domain.model.toEntity
import com.example.picknumberproject.view.search.SearchActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MapActivity : ViewBindingActivity<ActivityMapBinding>(), OnMapReadyCallback,
    Overlay.OnClickListener,
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

    private val databaseDao: BankDao by lazy {
        BankDatabase.getDatabase(this).getBankDao()
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
                                Log.d("marker update", "marker update")
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
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            banksEntity.map {
                val goal = "${it.longitude},${it.latitude}"
                try {
                    val response = RetrofitUtil.direction5Api.getDistance(
                        start = "126.9050532,37.4652659",
                        goal = goal
                    )
                    if (response.isSuccessful) {
                        val body = response.body()
                        check(body != null) { "body 응답이 없습니다." }
                        it.distance = body.route.traoptimal[0].summary.distance / 1000
                        it.duration = body.route.traoptimal[0].summary.duration / 1000 / 60
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            databaseDao.insertAll(banksEntity)
        }
        return banksEntity.sortedBy(BankEntity::distance)
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
                bank.name + "/" + bank.address + "/" + bank.distance + "/" + bank.duration + "/" + bank.code + "/" + bank.divisionCode + "/" + bank.tel
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
        job.cancel()
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