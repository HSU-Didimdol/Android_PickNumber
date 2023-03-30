package com.example.picknumberproject.domain.model

data class ReservationEntity(
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