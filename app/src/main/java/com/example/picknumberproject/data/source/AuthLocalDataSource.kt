package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.model.Login


interface AuthLocalDataSource {
    fun hasData(): Boolean
    fun getData(): Login?
    fun setData(loginData: Login)
    fun clear()
}
