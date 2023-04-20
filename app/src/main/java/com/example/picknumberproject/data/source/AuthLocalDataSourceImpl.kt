package com.example.picknumberproject.data.source

import android.content.SharedPreferences
import com.example.picknumberproject.data.model.Login
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Named


class AuthLocalDataSourceImpl @Inject constructor(
    @Named("auth") private val sharedPreferences: SharedPreferences
) : AuthLocalDataSource {

    private var data: Login? = null

    override fun hasData(): Boolean {
        return getData() != null
    }

    override fun getData(): Login? {
        // 이미 전에 데이터를 읽어 값이 있는 경우에는 곧바로 데이터를 반환한다.
        if (data != null) {
            return data
        }
        val json = sharedPreferences.getString(AUTH_LOCAL_DATA_PREFS_KEY, null) ?: return null
        data = Gson().fromJson(json, Login::class.java)
        return data
    }

    override fun setData(loginData: Login) {
        data = loginData
        val json = Gson().toJson(loginData)
        sharedPreferences.edit().putString(AUTH_LOCAL_DATA_PREFS_KEY, json).apply()
    }

    override fun clear() {
        data = null
        sharedPreferences.edit().remove(AUTH_LOCAL_DATA_PREFS_KEY).apply()
    }

    companion object {
        private const val AUTH_LOCAL_DATA_PREFS_KEY = "authLocalData"
    }
}