package com.example.picknumberproject.data.source

import com.example.picknumberproject.data.dto.company.CompanyDto

interface CompanyRemoteDataSource {

    suspend fun getCompanyList(): CompanyDto

}