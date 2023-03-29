package com.example.picknumberproject.data.requestBody.sms

data class Contents(
    val callback: String,
    val contents: String,
    val kind: String,
    val receiverTelNo: String,
    val type: String,
    val userKey: String
)