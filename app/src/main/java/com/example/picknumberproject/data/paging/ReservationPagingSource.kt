package com.example.picknumberproject.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.picknumberproject.data.api.MainServerApi
import com.example.picknumberproject.data.dto.reservation.toEntity
import com.example.picknumberproject.data.extension.errorMessage
import com.example.picknumberproject.data.model.ReservationBody
import com.example.picknumberproject.domain.model.ReservationEntity
import javax.inject.Inject

class ReservationPagingSource @Inject constructor(
    private val api: MainServerApi,
    private val reservationBody: ReservationBody
) : PagingSource<Int, ReservationEntity>() {

    companion object {
        private const val START_PAGE = 1
        const val PAGE_SIZE = 15
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReservationEntity> {
        val page = params.key ?: START_PAGE
        return try {
            val response = api.getReservationList(reservationBody = reservationBody)
            if (response.isSuccessful) {
                val reservations = response.body()!!.data.reservations.map { it.toEntity() }
                val isEnd = reservations.isEmpty()

                LoadResult.Page(
                    data = reservations,
                    prevKey = if (page == START_PAGE) null else page - 1,
                    nextKey = if (isEnd) null else page + 1
                )
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: java.lang.Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ReservationEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}