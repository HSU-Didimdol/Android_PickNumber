package com.example.picknumberproject.data.source

import com.example.picknumberproject.domain.model.CompanyEntity

interface Directions5RemoteDataSource {

    suspend fun getCompanyDistance(companyList: List<CompanyEntity>): List<CompanyEntity>

}