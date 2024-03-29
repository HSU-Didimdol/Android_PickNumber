package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.api.MainServerApi
import com.example.picknumberproject.data.db.UserDao
import com.example.picknumberproject.data.extension.getDataOrThrowMessage
import com.example.picknumberproject.data.model.Login
import com.example.picknumberproject.data.requestBody.sms.ContentBody
import com.example.picknumberproject.data.requestBody.sms.Contents
import com.example.picknumberproject.data.source.AuthLocalDataSource
import com.example.picknumberproject.domain.model.UserEntity
import com.example.picknumberproject.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val api: MainServerApi,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {

    override suspend fun signIn(userName: String, password: String): Result<Unit> {
        return try {
            localDataSource.setData(Login(userName = userName, password = password))
            userDao.getUserByNameAndPassword(name = userName, password = password)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun signUp(
        userName: String,
        password: String,
        phoneNumber: String
    ): Result<Unit> {
        return try {
            userDao.insertUser(
                UserEntity(
                    password = password,
                    name = userName,
                    phone = phoneNumber
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun checkAuthenticCode(
        phoneNumber: String,
        authenticCode: String
    ): Result<Unit> {
        return runCatching {
            val response = api.getSMSNotification(
                contentsBody = ContentBody(
                    Contents(
                        kind = "LANDPICK",
                        type = "개인정보인증",
                        callback = "15331490",
                        contents = "[순번관리시스템 본인인증] 인증번호 [$authenticCode]\n를 입력해 주세요",
                        receiverTelNo = phoneNumber,
                        userKey = ""
                    )
                )
            )
            response.getDataOrThrowMessage()
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            localDataSource.clear()
        }
    }

    override suspend fun checkLoggedIn(): Result<Boolean> {
        return try {
            if (localDataSource.hasData()) {
                Result.success(true)
            } else {
                Result.success(false)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUserInfo(): Result<List<UserEntity>> {
        return runCatching {
            val userDetail = userDao.getAll()
            userDetail
        }
    }

    override fun hasUserInfo(password: String): Boolean {
        val list = userDao.getPassword()
        return list.contains(password)
    }
}