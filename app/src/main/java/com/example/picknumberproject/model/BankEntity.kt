package com.example.picknumberproject.model

import android.os.Parcelable
import com.example.picknumberproject.dto.bank.BankDto
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BankEntity(
    val name: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    var distance: Double = 0.0,
    var duration: Int = 0,
    val code: String,
    val divisionCode: String,
    val tel: String
) : Parcelable

fun BankDto.toEntity() = BankEntity(
    name = "$name 새마을금고 $divisionName",
    address = address,
    longitude = longitude,
    latitude = latitude,
    distance = 0.0,
    duration = 0,
    code = code,
    divisionCode = divisionCode,
    tel = tel

)