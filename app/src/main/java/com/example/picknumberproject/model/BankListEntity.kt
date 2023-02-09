package com.example.picknumberproject.model

import com.example.picknumberproject.dto.bank.BankListDto


data class BankListEntity(
    val items: List<BankEntity>
)

fun BankListDto.toEntity() = BankListEntity(
    items = items.map {
        it.toEntity()
    }
)