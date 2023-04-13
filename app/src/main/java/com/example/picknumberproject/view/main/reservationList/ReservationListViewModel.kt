package com.example.picknumberproject.view.main.reservationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picknumberproject.R
import com.example.picknumberproject.domain.model.CompanyEntity
import com.example.picknumberproject.domain.repository.CompanyRepository
import com.example.picknumberproject.domain.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private var fetchJob: Job? = null

    private val phoneNumber: String = "010-5905-9620"

    init {
        fetchReservations()
    }

    fun fetchReservations() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            val dataList = reservationRepository.getAllReservationList(phoneNumber)
            if (dataList.isSuccess) {
                _uiState.update { data ->
                    data.copy(
                        reservations = dataList.getOrNull()!!.map {
                            val companyEntity = getCompanyNumber(it.companyID)
                            it.toUiState(
                                companyNumber = companyEntity.tel,
                                securityCode = companyEntity.securityKey,
                                longitude = companyEntity.longitude,
                                latitude = companyEntity.latitude
                            )
                        })
                }
            } else {
                _uiState.update { it.copy(userMessage = dataList.exceptionOrNull()!!.localizedMessage?.toInt()) }
            }
        }
    }

    private suspend fun getCompanyNumber(code: Int): CompanyEntity {
        val result = companyRepository.getCompanyEntity("%$code%")
        return if (result.isSuccess) {
            result.getOrDefault(
                CompanyEntity(
                    "",
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "", ""
                )
            )
        } else {
            CompanyEntity(
                "",
                "",
                0,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "", ""
            )
        }

    }

    fun reservationDelete(uiState: ReservationItemUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = reservationRepository.deleteReservationItem(
                uiState.companyID,
                uiState.reservationID,
                uiState.securityCode
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
            fetchReservations()
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}