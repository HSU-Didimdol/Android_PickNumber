package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.api.MainServerApi
import com.example.picknumberproject.data.dto.delete.DeleteDto
import com.example.picknumberproject.data.dto.reservation.ReservationsDto
import com.example.picknumberproject.data.extension.ResponseBody
import com.example.picknumberproject.data.requestBody.deleteReservation.DeleteBody
import com.example.picknumberproject.data.requestBody.reservation.ReservationBody
import retrofit2.Response
import javax.inject.Inject

class ReservationRemoteSourceImpl @Inject constructor(
    private val mainServerApi: MainServerApi
) : ReservationRemoteSource {

    override suspend fun getReservationList(reservationBody: ReservationBody): Response<ResponseBody<ReservationsDto>> =
        mainServerApi.getReservationList(reservationBody = reservationBody)

    override suspend fun deleteReservationItem(
        token: String,
        deleteBody: DeleteBody
    ): Response<DeleteDto> =
        mainServerApi.deleteReservation(
            x_access_token = token,
            deleteBody = deleteBody
        )
}