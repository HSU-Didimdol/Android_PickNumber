package com.example.picknumberproject.data.extension

import com.google.gson.Gson
import retrofit2.Response

/**
 * ResponseBody를 하나의 데이터 클래스로 만들어서 공통된 부분을 묶고 예외처리와 에러메세지를 반환받기 쉽게 만들었습니다.
 * 다만 전에 하던 프로젝트에서
 */

data class ResponseBody<T>(
    val success: Boolean,
    val results: T
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
        return requireNotNull(body()).results
    } else {
        throw Exception(errorMessage)
    }
}
