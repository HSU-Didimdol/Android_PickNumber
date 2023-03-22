package com.example.picknumberproject.data.extension

data class ResponseBody<T>(
    val success: Boolean,
    val data: T
)