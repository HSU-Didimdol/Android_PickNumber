package com.example.picknumberproject.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.picknumberproject.R
import com.example.picknumberproject.databinding.ActivityMainBinding
import com.example.picknumberproject.domain.model.CompanyEntity
import com.example.picknumberproject.view.common.ViewBindingActivity
import com.example.picknumberproject.view.extension.hideKeyboard
import com.example.picknumberproject.view.home.HomeActivity
import com.example.picknumberproject.view.main.map.MapFragment
import com.example.picknumberproject.view.main.search.SearchFragment
import com.example.picknumberproject.view.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    companion object {
        fun getIntent(context: Context, query: String): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra("query", query)
            }
        }
    }

    private fun getQuery(): String {
        return intent.getStringExtra("query")!!
    }

    private var frontQuery: String? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        frontQuery = getQuery()
        setSupportActionBar(toolbar)
        initSearchFragment()
        initSearchView()

        infoUpdateButton.setOnClickListener {
            navigateToInfoUpdateView()
        }

        binding.homeButton.setOnClickListener {
            HomeActivity.getIntent(this)
            finish()
        }
    }

    private fun initSearchView() {
        binding.searchView.setQuery(frontQuery, false)
        binding.searchView.isSubmitButtonEnabled = true
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (viewModel.uiState.value.searchToMap) {
                        val company = emptyList<CompanyEntity>()
                        viewModel.updateMapToSearch()
                        replaceMapFragment(company)
                        hideKeyboard()
                    } else {
                        replaceSearchFragment(query)
                        viewModel.updateSearchToMap()
                        frontQuery = query
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initSearchFragment() {
        val fragmentManager = supportFragmentManager
        val searchFragment = SearchFragment(getQuery())
        fragmentManager.beginTransaction().apply {
            replace(R.id.container_view, searchFragment)
        }.commit()
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_view, fragment).addToBackStack(null).commit()
    }

    fun replaceSearchFragment(query: String) {
        val fragmentManager = supportFragmentManager
        val searchFragment = SearchFragment(query)
        fragmentManager.beginTransaction().apply {
            replace(R.id.container_view, searchFragment)
        }.commit()
    }

    fun replaceMapFragment(company: List<CompanyEntity>) {
        val fragmentManager = supportFragmentManager
        val mapFragment = MapFragment(company)
        fragmentManager.beginTransaction().apply {
            replace(R.id.container_view, mapFragment)
        }.commit()
    }

    private fun navigateToInfoUpdateView() {
        val intent = SignUpActivity.getIntent(this, "infoUpdate")
        startActivity(intent)
    }
}
