package com.example.picknumberproject.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.picknumberproject.data.dto.bank.BankDto
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "bank")
data class BankEntity(
    @PrimaryKey val code: String,
    val name: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    var distance: Double = 0.0,
    var duration: Int = 0,
    val divisionCode: String,
    val tel: String
) : Parcelable

fun BankDto.toEntity() = BankEntity(
    code = code,
    name = "$name 새마을금고 $divisionName",
    address = address,
    longitude = longitude,
    latitude = latitude,
    distance = 0.0,
    duration = 0,
    divisionCode = divisionCode,
    tel = tel

)