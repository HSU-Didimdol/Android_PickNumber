package com.example.picknumberproject.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
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
    }

    private val viewModel: HomeViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setSupportActionBar(toolbar)

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
