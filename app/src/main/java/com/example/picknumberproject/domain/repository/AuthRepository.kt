package com.example.picknumberproject.domain.repository

import com.example.picknumberproject.domain.model.UserEntity

interface AuthRepository {

    suspend fun signIn(userName: String, password: String): Result<Unit>

    fun hasUserInfo(password: String): Boolean

    suspend fun signUp(userName: String, password: String, phoneNumber: String): Result<Unit>

    suspend fun checkAuthenticCode(
        phoneNumber: String,
        authenticCode: String
    ): Result<Unit>

    suspend fun getCurrentUserInfo(): Result<List<UserEntity>>
}