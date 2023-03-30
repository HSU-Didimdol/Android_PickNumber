package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.dto.delete.DeleteDto
import com.example.picknumberproject.data.dto.reservation.ReservationsDto
import com.example.picknumberproject.data.extension.ResponseBody
import com.example.picknumberproject.data.requestBody.deleteReservation.DeleteBody
import com.example.picknumberproject.data.requestBody.reservation.ReservationBody
import retrofit2.Response

interface ReservationRemoteSource {

    suspend fun getReservationList(reservationBody: ReservationBody): Response<ResponseBody<ReservationsDto>>

    suspend fun deleteReservationItem(
        token: String,
        deleteBody: DeleteBody,
    ): Response<DeleteDto>
}