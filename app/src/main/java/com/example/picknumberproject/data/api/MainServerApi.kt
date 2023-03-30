package com.example.picknumberproject.data.api

import com.example.picknumberproject.data.dto.delete.DeleteDto
import com.example.picknumberproject.data.dto.reservation.ReservationsDto
import com.example.picknumberproject.data.dto.sms.ContentDto
import com.example.picknumberproject.data.extension.ResponseBody
import com.example.picknumberproject.data.requestBody.deleteReservation.DeleteBody
import com.example.picknumberproject.data.requestBody.reservation.ReservationBody
import com.example.picknumberproject.data.requestBody.sms.ContentBody
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
    ): Response<ResponseBody<ReservationsDto>>


    @DELETE(Url.DELETE_RESERVATION)
    suspend fun deleteReservation(
        @Header("x-access-token") x_access_token: String,
        @Body deleteBody: DeleteBody
    ): Response<DeleteDto>


    @POST(Url.POST_NOTIFICATION_SMS)
    suspend fun getSMSNotification(
        @Header("x-access-token") x_access_token: String = Key.reservation_token,
        @Body contentsBody: ContentBody
    ): Response<ResponseBody<ContentDto>>

}