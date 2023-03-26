package com.example.picknumberproject.view.main.reservationList

import androidx.paging.PagingData
import com.example.picknumberproject.domain.model.ReservationEntity

data class ReservationListUiState(
    val pagingData: PagingData<ReservationItemUiState> = PagingData.empty()
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
    val workGroupName: String
)

fun ReservationEntity.toUiState() = ReservationItemUiState(
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
    workGroupName = workGroupName
)