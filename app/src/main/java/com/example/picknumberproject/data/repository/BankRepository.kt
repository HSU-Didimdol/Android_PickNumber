package com.example.picknumberproject.data.repository

import com.example.picknumberproject.data.api.RetrofitUtil
import com.example.picknumberproject.data.db.BankDao
import com.example.picknumberproject.data.db.BankDatabase
import com.example.picknumberproject.domain.model.BankEntity
import com.example.picknumberproject.domain.model.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  Repository의 궁극적인 목표는 Domain에게 직접적으로 Data를 흘려보내주는 역할을 합니다.
 *  다만, 이번 프로젝트에서는 의존성주입을 전부 제외하였기에, Repository의 역할은 data를 곧바로 viewModel로 쏘아줍니다.
 */
object BankRepository {
    private val bankDao: BankDao
        get() {
            return BankDatabase.getDatabase().getBankDao()
        }

    private suspend fun getBankList() = withContext(Dispatchers.IO) {
        val response = RetrofitUtil.bankApi.getBankList()
        if (response.isSuccessful && response.body() != null) {
            requireNotNull(response.body())
        } else {
            emptyList()
        }
    }

    private suspend fun getBankDistance(bankList: List<BankEntity>) = withContext(Dispatchers.IO) {
        bankList.map { bank ->
            val goal = "${bank.longitude},${bank.latitude}"
            val response =
                RetrofitUtil.direction5Api.getDistance(
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
        bankDao.insertAll(bankList)
        bankList
    }

    suspend fun getAllBankEntityList(): List<BankEntity> {
        val dataList = getBankList()
        return getBankDistance(dataList.map { it.toEntity() })
    }
}
