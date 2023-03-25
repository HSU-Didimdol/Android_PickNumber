package com.example.picknumberproject.view.main.reservationList

import androidx.paging.PagingData
import com.example.picknumberproject.domain.model.ReservationEntity

data class ReservationListUiState(
    val pagingData: PagingData<ReservationEntity> = PagingData.empty()
)