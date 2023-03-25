package com.example.picknumberproject.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.picknumberproject.data.api.MainServerApi
import com.example.picknumberproject.data.model.Reservation
import com.example.picknumberproject.data.model.ReservationBody
import com.example.picknumberproject.data.paging.ReservationPagingSource
import com.example.picknumberproject.data.paging.ReservationPagingSource.Companion.PAGE_SIZE
import com.example.picknumberproject.domain.model.ReservationEntity
import com.example.picknumberproject.domain.repository.ReservationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val api: MainServerApi
) : ReservationRepository {

    override fun getAllReservationList(): Flow<PagingData<ReservationEntity>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = {
                ReservationPagingSource(
                    api = api, reservationBody = ReservationBody(
                        reservation = Reservation(
                            date = null,
                            phoneNumber = "010-5905-9620",
                            workGroupID = null
                        )
                    )
                )
            }
        ).flow
    }

}