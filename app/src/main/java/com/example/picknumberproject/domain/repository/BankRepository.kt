package com.example.picknumberproject.domain.repository

import com.example.picknumberproject.domain.model.BankEntity

interface BankRepository {

    suspend fun getAllBankEntityList(): List<BankEntity>

}