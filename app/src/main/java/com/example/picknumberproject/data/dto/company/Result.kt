package com.example.picknumberproject.data.dto.company

import com.example.picknumberproject.domain.model.CompanyEntity

data class Result(
    val address: String,
    val code: String,
    val companyID: Int,
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
    val use: String
)

fun Result.toEntity() = CompanyEntity(
    address = address,
    code = code,
    companyID = companyID,
    divisionCode = divisionCode,
    divisionName = divisionName,
    email = email,
    fax = fax,
    gugun = gugun,
    latitude = latitude,
    longitude = longitude,
    modifyDate = modifyDate,
    name = name,
    registrationDate = registrationDate,
    securityKey = securityKey,
    sido = sido,
    tel = tel,
    type = type,
    use = use,
    searchQuery = "$address 새마을금고 $divisionName",
    distance = "",
    duration = ""
)