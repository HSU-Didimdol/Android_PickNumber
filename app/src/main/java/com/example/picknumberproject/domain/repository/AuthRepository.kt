package com.example.picknumberproject.domain.repository

interface AuthRepository {

    suspend fun signIn(userName: String, password: String): Result<Unit>

    fun hasUserInfo(password: String): Boolean

    suspend fun signUp(userName: String, password: String, phoneNumber: String): Result<Unit>

    suspend fun checkAuthenticCode(
        phoneNumber: String,
        authenticCode: String
    ): Result<Unit>
}