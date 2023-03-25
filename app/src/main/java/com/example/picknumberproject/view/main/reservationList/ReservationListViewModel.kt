package com.example.picknumberproject.view.main.reservationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.picknumberproject.domain.repository.CompanyRepository
import com.example.picknumberproject.domain.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationListViewModel @Inject constructor(
    private val companyRepository: CompanyRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ReservationListUiState()
    )
    val uiState = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        fetchReservation()
    }

    private fun fetchReservation() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            reservationRepository.getAllReservationList().cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                _uiState.update {
                    it.copy(pagingData = pagingData)
                }
            }
        }
    }

}