package com.example.picknumberproject.data.source

import com.example.picknumberproject.domain.model.BankEntity

interface Directions5RemoteDataSource {

    suspend fun getBankDistance(bankList: List<BankEntity>): List<BankEntity>

}