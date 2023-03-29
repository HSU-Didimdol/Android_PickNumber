package com.example.picknumberproject.domain.repository

interface AuthRepository {

    suspend fun signIn(userName: String, phoneNumber: String): Result<Unit>

    fun hasUserInfo(phoneNumber: String): Boolean

    suspend fun signUp(userName: String, phoneNumber: String): Result<Unit>

    suspend fun checkAuthenticCode(phoneNumber: String, authenticCode: String): Result<Unit>
}