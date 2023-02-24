package com.example.picknumberproject.data.di

import com.example.picknumberproject.data.source.BankRemoteDataSource
import com.example.picknumberproject.data.source.BankRemoteDataSourceImpl
import com.example.picknumberproject.data.source.Directions5RemoteDataSource
import com.example.picknumberproject.data.source.Directions5RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {

    @Singleton
    @Binds
    abstract fun bindsBankRemoteDataSource(
        bankRemoteDataSourceImpl: BankRemoteDataSourceImpl
    ): BankRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindsDirections5RemoteDataSource(
        directions5RemoteDataSourceImpl: Directions5RemoteDataSourceImpl
    ): Directions5RemoteDataSource

}