package com.example.picknumberproject.dto.bank

data class BankDto(
    val code: String,
    val divisionCode: String,
    val companyID: Int,
    val category: Int,
    val type: String,
    val name: String,
    val divisionName: String,
    val tel: String,
    val fax: String,
    val email: String,
    val sido: String,
    val gugun: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
    val startTime: String,
    val endTime: String,
    val memo: String,
    val use: String = "Y",
    val modifyDate: String,
    val registrationDate: String,
    var distance: Int = 0
)
