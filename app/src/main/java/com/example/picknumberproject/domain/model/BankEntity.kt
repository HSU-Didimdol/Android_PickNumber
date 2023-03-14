package com.example.picknumberproject.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.picknumberproject.data.dto.company.CompanyDto
import kotlinx.android.parcel.Parcelize

/**
 *  Domain에서 toEntity는 Data Layer에서 흘러온 Data들을 우리가 사용하기에 알맞은 형태로 변환 시켜준다.
 */
@Parcelize
@Entity(tableName = "bank_table")
data class BankEntity(
    @PrimaryKey val code: Int,
    val name: String,
    val divisionCode: Int,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    var distance: Double = 0.0,
    var duration: Int = 0,
    val tel: String
) : Parcelable

fun CompanyDto.toEntity() = BankEntity(
    code = code,
    name = "$name ($divisionName)",
    divisionCode = divisionCode,
    address = address,
    longitude = longitude,
    latitude = latitude,
    distance = 0.0,
    duration = 0,
    tel = tel
)