package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.api.Direction5Api
import com.example.picknumberproject.data.di.annotation.IoDispatcher
import com.example.picknumberproject.domain.model.BankEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Directions5RemoteDataSourceImpl @Inject constructor(
    private val direction5Api: Direction5Api,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : Directions5RemoteDataSource {

    override suspend fun getBankDistance(bankList: List<BankEntity>) = withContext(ioDispatcher) {
        bankList.map { bank ->
            val goal = "${bank.longitude},${bank.latitude}"
            val response =
                direction5Api.getDistance(
                    start = "126.9050532,37.4652659", // TODO:이건 추후 수정바람 내 위치 따라 유동적으로 바뀌게끔
                    goal = goal
                )
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()
                check(body != null) { "body 응답이 없습니다." }
                bank.distance = body.route.traoptimal[0].summary.distance / 1000
                bank.duration = body.route.traoptimal[0].summary.duration / 1000 / 60
            } else {
                bank.distance = 0.0
                bank.duration = 0
            }
        }
        bankList
    }
}