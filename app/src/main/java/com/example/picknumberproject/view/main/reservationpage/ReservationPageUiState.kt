package com.example.picknumberproject.view.main.reservationpage

import com.example.picknumberproject.domain.model.UserEntity

data class ReservationPageUiState(
    val url: String = "",
    val userInfo: UserEntity? = null
)