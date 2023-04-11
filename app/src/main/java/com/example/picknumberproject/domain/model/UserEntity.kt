package com.example.picknumberproject.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val phone: String,
    val name: String,
    val password: String
)
