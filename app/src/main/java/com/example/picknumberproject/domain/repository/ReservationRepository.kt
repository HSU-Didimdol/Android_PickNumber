package com.example.picknumberproject.domain.repository

import com.example.picknumberproject.domain.model.ReservationEntity

interface ReservationRepository {

    suspend fun getAllReservationList(phoneNumber: String): Result<List<ReservationEntity>>

    suspend fun deleteReservationItem(
        companyID: Int,
        reservationID: Int,
        token: String
    ): Result<Unit>

}