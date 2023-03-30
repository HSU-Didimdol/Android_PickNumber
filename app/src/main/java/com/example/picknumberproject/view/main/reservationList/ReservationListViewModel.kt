package com.example.picknumberproject.view.main.reservationList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.R
import com.example.picknumberproject.domain.repository.CompanyRepository
import com.example.picknumberproject.domain.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationListViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val companyRepository: CompanyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ReservationListUiState()
    )
    val uiState = _uiState.asStateFlow()
    private var bounded = false

    fun bind(
        initData: MutableList<ReservationItemUiState>?
    ) {
        if (bounded) return
        bounded = true
        if (initData != null) {
            _uiState.update { it.copy(reservations = initData) }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val dataList = reservationRepository.getAllReservationList()
            if (dataList.isSuccess) {
                _uiState.update { data ->
                    data.copy(
                        reservations = dataList.getOrNull()!!.map {
                            val phoneNumber = getCompanyNumber(it.companyID)
                            val securityCode = getSecurityKey(it.companyID)
                            it.toUiState(phoneNumber, securityCode)
                        })
                }
            } else {
                _uiState.update { it.copy(userMessage = dataList.exceptionOrNull()!!.localizedMessage?.toInt()) }
            }
        }
    }

    private suspend fun getCompanyNumber(code: Int): String {
        val result = companyRepository.getCompanyNumber("%$code%")
        return if (result.isSuccess) {
            result.getOrDefault("")
        } else {
            ""
        }

    }

    private suspend fun getSecurityKey(code: Int): String {
        val result = companyRepository.getValidCode("%$code%")
        return if (result.isSuccess) {
            result.getOrDefault("")
        } else {
            ""
        }
    }

    fun reservationDelete(uiState: ReservationItemUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("securityKey", uiState.securityCode)
            Log.d("companyID", uiState.companyID.toString())
            Log.d("reservationID", uiState.reservationID.toString())
            val result = reservationRepository.deleteReservationItem(
                uiState.companyID,
                uiState.reservationID,
                uiState.securityCode
            )
            Log.d("result", result.toString())
            _uiState.update {
                it.copy(
                    userMessage = if (result.isSuccess) {
                        R.string.reservation_deleted
                    } else {
                        R.string.failed
                    }
                )
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

}