package com.example.picknumberproject.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  Domain에서 toEntity는 Data Layer에서 흘러온 Data들을 우리가 사용하기에 알맞은 형태로 변환 시켜준다.
 */
@Entity(tableName = "company_table")
data class CompanyEntity(
    val address: String,
    @PrimaryKey val code: String,
    val companyID: Int,
    val searchQuery: String,
    val divisionCode: String,
    val divisionName: String,
    val email: String,
    val fax: String,
    val gugun: String,
    val latitude: String,
    val longitude: String,
    val modifyDate: String,
    val name: String,
    val registrationDate: String,
    val securityKey: String,
    val sido: String,
    val tel: String,
    val type: String,
    val use: String,
    val distance: String,
    val duration: String
) : java.io.Serializable

