package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.api.MainServerApi
import com.example.picknumberproject.data.db.UserDao
import com.example.picknumberproject.data.extension.getDataOrThrowMessage
import com.example.picknumberproject.data.requestBody.sms.ContentBody
import com.example.picknumberproject.data.requestBody.sms.Contents
import com.example.picknumberproject.domain.model.UserEntity
import com.example.picknumberproject.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val api: MainServerApi
) : AuthRepository {

    override suspend fun signIn(userName: String, phoneNumber: String): Result<Unit> {
        return try {
            userDao.getUserByPhone(phoneNumber).isNotEmpty()
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun signUp(
        userName: String,
        phoneNumber: String
    ): Result<Unit> {
        return try {
            userDao.insertUser(UserEntity(phone = phoneNumber, name = userName))
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

    override fun hasUserInfo(phoneNumber: String): Boolean {
        val list = userDao.getPhone()
        return list.contains(phoneNumber)
    }

}