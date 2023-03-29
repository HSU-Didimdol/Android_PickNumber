package com.example.picknumberproject.domain.repository

import com.example.picknumberproject.domain.model.ReservationEntity

interface ReservationRepository {

    suspend fun getAllReservationList(): Result<List<ReservationEntity>>

}