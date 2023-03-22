package com.example.picknumberproject.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivityMainBinding
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.view.main.reservationList.ReservationListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val viewModel: MainViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setSupportActionBar(toolbar)

        initReservationListFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                Toast.makeText(this, "셋팅 버튼 클릭", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
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
}