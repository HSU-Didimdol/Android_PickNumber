package com.example.picknumberproject.domain.repository

import androidx.paging.PagingData
import com.example.picknumberproject.domain.model.ReservationEntity
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {

    fun getAllReservationList(): Flow<PagingData<ReservationEntity>>
    
}