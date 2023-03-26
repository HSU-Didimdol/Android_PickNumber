package com.example.picknumberproject.view.main.reservationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
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
    private val companyRepository: CompanyRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ReservationListUiState()
    )
    val uiState = _uiState.asStateFlow()
    private var bounded = false

    fun bind(
        initPostPagingData: PagingData<ReservationItemUiState>?
    ) {
        if (bounded) return
        bounded = true
        if (initPostPagingData != null) {
            _uiState.update { it.copy(pagingData = initPostPagingData) }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val pagingFlow = reservationRepository.getAllReservationList()
            pagingFlow.cachedIn(viewModelScope)
                .collect { pagingData ->
                    _uiState.update { uiState ->
                        uiState.copy(pagingData = pagingData.map { it.toUiState() })
                    }
                }
        }
    }
}