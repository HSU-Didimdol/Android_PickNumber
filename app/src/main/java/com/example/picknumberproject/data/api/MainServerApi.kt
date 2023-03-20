package com.example.picknumberproject.data.api

import com.example.picknumberproject.data.dto.reservation.ReservationDto
import com.example.picknumberproject.data.model.ReservationBody
import com.example.picknumberproject.data.url.Key
import com.example.picknumberproject.data.url.Url
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface MainServerApi {

    @POST(Url.POST_RESERVATION)
    suspend fun getReservationList(
        @Header("x-access-token") x_access_token: String = Key.x_access_token,
        @Body reservationBody: ReservationBody
    ): Response<ReservationDto>


    @DELETE(Url.DIRECTION5_URL)
    suspend fun deleteReservation(
        @Header("x-access-token") x_access_token: String
    )
}