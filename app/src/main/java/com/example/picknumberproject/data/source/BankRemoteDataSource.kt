package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.dto.bank.BankDto

interface BankRemoteDataSource {

    suspend fun getBankList(): List<BankDto>

}