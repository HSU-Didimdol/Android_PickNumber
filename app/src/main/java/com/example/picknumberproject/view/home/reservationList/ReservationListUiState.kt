package com.example.picknumberproject.view.home.reservationList

import com.example.picknumberproject.domain.model.ReservationEntity

data class ReservationListUiState(
    val userMessage: Int? = null,
    val reservations: List<ReservationItemUiState> = emptyList()
)

data class ReservationItemUiState(
    val companyID: Int,
    val companyName: String,
    val date: String,
    val hour: String,
    val information: String,
    val minute: String,
    val name: String,
    val phoneNumber: String,
    val policyAgreement: String,
    val registrationDate: String,
    val reservationID: Int,
    val state: String,
    val validTimeAfter: Int,
    val validTimeBefore: Int,
    val workGroupID: Int,
    val workGroupName: String,
    val companyNumber: String = "",
    val securityCode: String = "",
    val longitude: String = "",
    val latitude: String = ""
)

fun ReservationEntity.toUiState(
    companyNumber: String,
    securityCode: String,
    longitude: String,
    latitude: String
) =
    ReservationItemUiState(
        companyID = companyID,
        companyName = companyName,
        date = date,
        hour = hour,
        information = information,
        minute = minute,
        name = name,
        phoneNumber = phoneNumber,
        policyAgreement = policyAgreement,
        registrationDate = registrationDate,
        reservationID = reservationID,
        state = state,
        validTimeAfter = validTimeAfter,
        validTimeBefore = validTimeBefore,
        workGroupID = workGroupID,
        workGroupName = workGroupName,
        companyNumber = companyNumber,
        securityCode = securityCode,
        longitude = longitude,
        latitude = latitude
    )