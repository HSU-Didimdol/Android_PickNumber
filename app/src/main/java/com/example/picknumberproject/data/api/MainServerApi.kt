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
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * MainServerApi의 interface 입니다. 예약과 관련 getReservationList함수와 deleteReservation함수를 담고있습니다.
 * deleteReservation함수의 경우 @DELETE가 제대로 작동하지 않는 문제가 발생하여, 검색해보니
 * @HTTP(method = "DELETE", path = "~~~", hasBody = true)라고 쓰는게 좋다고 합니다.
 * 또한 인증번호를 위한 getSMSNotification함수 또한 담고있습니다.
 * 이에 대한 Rrtrofit에 대한 객체는 Network 모듈에 담고있습니다.
 */

interface MainServerApi {

    @POST(Url.POST_RESERVATION)
    suspend fun getReservationList(
        @Header("x-access-token") x_access_token: String = Key.x_access_token,
        @Body reservationBody: ReservationBody
    ): Response<ResponseBody<ReservationsDto>>


    @HTTP(method = "DELETE", path = Url.DELETE_RESERVATION, hasBody = true)
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