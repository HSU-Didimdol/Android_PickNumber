package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.db.BankDao
import com.example.picknumberproject.domain.model.BankEntity
import javax.inject.Inject

class BankLocalDataSourceImpl @Inject constructor(
    private val bankDao: BankDao
) : BankLocalDataSource {

    private var data: List<BankEntity>? = null


    override fun hasData(): Boolean {
        return getData() != null
    }

    override fun getData(): List<BankEntity>? {
        if (data != null) {
            return data
        }
        return bankDao.getAll()

    }

    override suspend fun setData(bankLocalData: List<BankEntity>) {
        data = bankLocalData
        bankDao.insertAll(data!!)
    }

    override suspend fun clear() {
        data = null
        bankDao.deleteAll()
    }

}