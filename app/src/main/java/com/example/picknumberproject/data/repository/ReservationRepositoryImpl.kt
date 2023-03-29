package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.dto.reservation.toEntity
import com.example.picknumberproject.data.extension.getDataOrThrowMessage
import com.example.picknumberproject.data.requestBody.reservation.Reservation
import com.example.picknumberproject.data.requestBody.reservation.ReservationBody
import com.example.picknumberproject.data.source.ReservationRemoteSource
import com.example.picknumberproject.domain.model.ReservationEntity
import com.example.picknumberproject.domain.repository.ReservationRepository
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val reservationRemoteSource: ReservationRemoteSource
) : ReservationRepository {

    override suspend fun getAllReservationList(): Result<List<ReservationEntity>> {
        return runCatching {
            val response = reservationRemoteSource.getReservationList(
                reservationBody = ReservationBody(
                    Reservation(
                        date = null,
                        workGroupID = null,
                        phoneNumber = "010-5905-9620"
                    )
                )
            )
            response.getDataOrThrowMessage().reservations.map { it.toEntity() }
        }
    }
}