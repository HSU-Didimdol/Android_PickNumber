package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.api.Direction5Api
import com.example.picknumberproject.data.di.annotation.IoDispatcher
import com.example.picknumberproject.domain.model.CompanyEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Directions5RemoteDataSourceImpl @Inject constructor(
    private val direction5Api: Direction5Api,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : Directions5RemoteDataSource {

    override suspend fun getCompanyDistance(companyList: List<CompanyEntity>) = withContext(ioDispatcher) {
        companyList.map { company ->
            val goal = "${company.longitude},${company.latitude}"
            val response =
                direction5Api.getDistance(
                    start = "126.9050532,37.4652659", // TODO:이건 추후 수정바람 내 위치 따라 유동적으로 바뀌게끔
                    goal = goal
                )
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()
                check(body != null) { "body 응답이 없습니다." }
                company.distance = body.route.traoptimal[0].summary.distance / 1000
                company.duration = body.route.traoptimal[0].summary.duration / 1000 / 60
            } else {
                company.distance = 0.0
                company.duration = 0
            }
        }
        companyList
    }
}