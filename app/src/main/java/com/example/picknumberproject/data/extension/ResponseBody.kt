package com.example.picknumberproject.data.extension

import com.google.gson.Gson
import retrofit2.Response

data class ResponseBody<T>(
    val success: Boolean,
    val data: T
)

val <T> Response<ResponseBody<T>>.errorMessage: String?
    get() {
        if (isSuccessful) throw IllegalStateException()
        return try {
            message()
            val errorBodyJson = requireNotNull(errorBody()).string()
            val responseBody = Gson().fromJson(errorBodyJson, ResponseBody::class.java)
            responseBody.success.toString()
        } catch (e: Exception) {
            e.message
        }
    }

fun <T> Response<ResponseBody<T>>.getDataOrThrowMessage(): T {
    if (isSuccessful) {
        return requireNotNull(body()).data
    } else {
        throw Exception(errorMessage)
    }
}
