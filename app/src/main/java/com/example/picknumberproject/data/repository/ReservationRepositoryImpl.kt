package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.db.UserDao
import com.example.picknumberproject.data.dto.reservation.toEntity
import com.example.picknumberproject.data.extension.getDataOrThrowMessage
import com.example.picknumberproject.data.requestBody.deleteReservation.DeleteBody
import com.example.picknumberproject.data.requestBody.reservation.Reservation
import com.example.picknumberproject.data.requestBody.reservation.ReservationBody
import com.example.picknumberproject.data.source.ReservationRemoteSource
import com.example.picknumberproject.domain.model.ReservationEntity
import com.example.picknumberproject.domain.repository.ReservationRepository
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val reservationRemoteSource: ReservationRemoteSource,
    private val userDao: UserDao
) : ReservationRepository {

    override suspend fun getAllReservationList(): Result<List<ReservationEntity>> {
        return runCatching {
            val phone = userDao.getAll()[0].phone
            val response = reservationRemoteSource.getReservationList(
                reservationBody = ReservationBody(
                    Reservation(
                        date = null,
                        workGroupID = null,
                        phoneNumber = phone
                    )
                )
            )
            response.getDataOrThrowMessage().reservations.map { it.toEntity() }
        }
    }

    override suspend fun deleteReservationItem(
        companyID: Int,
        reservationID: Int,
        token: String
    ): Result<Unit> {
        return runCatching {
            val response = reservationRemoteSource.deleteReservationItem(
                token = token,
                deleteBody = DeleteBody(
                    reservation = com.example.picknumberproject.data.requestBody.deleteReservation.Reservation(
                        companyID = companyID,
                        reservationID = reservationID
                    )
                )
            )
            response
        }
    }
}