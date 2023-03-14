package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.db.CompanyDao
import com.example.picknumberproject.domain.model.BankEntity
import javax.inject.Inject

class CompanyLocalDataSourceImpl @Inject constructor(
    private val companyDao: CompanyDao
) : CompanyLocalDataSource {

    private var data: List<BankEntity>? = null


    override fun hasData(): Boolean {
        return getData() != null
    }

    override fun getData(): List<BankEntity>? {
        if (data != null) {
            return data
        }
        return companyDao.getAll()

    }

    override suspend fun setData(bankLocalData: List<BankEntity>) {
        data = bankLocalData
        companyDao.insertAll(data!!)
    }

    override suspend fun clear() {
        data = null
        companyDao.deleteAll()
    }

}