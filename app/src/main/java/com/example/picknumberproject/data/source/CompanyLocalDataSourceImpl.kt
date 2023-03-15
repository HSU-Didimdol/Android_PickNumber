package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.db.CompanyDao
import com.example.picknumberproject.domain.model.CompanyEntity
import javax.inject.Inject

class CompanyLocalDataSourceImpl @Inject constructor(
    private val companyDao: CompanyDao
) : CompanyLocalDataSource {

    private var data: List<CompanyEntity>? = null


    override fun hasData(): Boolean {
        return getData() != null
    }

    override fun getData(): List<CompanyEntity>? {
        if (data != null) {
            return data
        }
        return companyDao.getAll()

    }

    override suspend fun setData(companyLocalData: List<CompanyEntity>) {
        data = companyLocalData
        companyDao.insertAll(data!!)
    }

    override suspend fun clear() {
        data = null
        companyDao.deleteAll()
    }

}