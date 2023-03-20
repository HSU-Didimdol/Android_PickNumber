package com.example.picknumberproject.data.di

import com.example.picknumberproject.data.source.CompanyLocalDataSource
import com.example.picknumberproject.data.source.CompanyLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalModule {

    @Singleton
    @Binds
    abstract fun bindsCompanyLocalDataSource(
        companyLocalDataSourceImpl: CompanyLocalDataSourceImpl
    ): CompanyLocalDataSource

}