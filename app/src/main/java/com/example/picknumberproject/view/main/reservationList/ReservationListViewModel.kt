package com.example.picknumberproject.view.main.reservationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.R
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
    private val reservationRepository: ReservationRepository
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
                        reservations = dataList.getOrNull()!!.map { it.toUiState() })
                }
            } else {
                _uiState.update { it.copy(userMessage = dataList.exceptionOrNull()!!.localizedMessage?.toInt()) }
            }
        }
    }

    fun reservationDelete(uiState: ReservationItemUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = reservationRepository.deleteReservationItem(
                uiState.companyID,
                uiState.reservationID
            )
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