package com.example.picknumberproject.view.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivityHomeBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.view.extension.hideKeyboard
import com.example.picknumberproject.view.home.reservationList.ReservationListFragment
import com.example.picknumberproject.view.main.MainActivity
import com.example.picknumberproject.view.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class HomeActivity : ViewBindingActivity<ActivityHomeBinding>() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private val viewModel: HomeViewModel by viewModels()
    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setSupportActionBar(toolbar)

        if (!isLocationPermissionGranted()) {
            requestLocationPermission()
        }


        initReservationListFragment()
        initSearchView()
        infoUpdateButton.setOnClickListener {
            navigateToInfoUpdateView()
        }

        homeButton.setOnClickListener {
            initReservationListFragment()
        }
    }

    private fun initSearchView() {
        binding.searchView.isSubmitButtonEnabled = true
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        navigateSearchActivity(query)
                        hideKeyboard()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@HomeActivity, "위치 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@HomeActivity, "위치 권한이 승인되지 않았습니다..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@HomeActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this@HomeActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun navigateSearchActivity(query: String) {
        val intent = MainActivity.getIntent(this@HomeActivity, query)
        startActivity(intent)
    }

    private fun initReservationListFragment() {
        val fragmentManager = supportFragmentManager
        val reservationListFragment = ReservationListFragment()
        fragmentManager.beginTransaction().apply {
            replace(R.id.container_view, reservationListFragment)
        }.commit()
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction
            .replace(R.id.container_view, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToInfoUpdateView() {
        val intent = SignUpActivity.getIntent(this, "infoUpdate")
        startActivity(intent)
    }
}
