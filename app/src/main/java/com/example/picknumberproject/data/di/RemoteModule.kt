package com.example.picknumberproject.data.di

import com.example.picknumberproject.data.source.*
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
    abstract fun bindsCompanyRemoteDataSource(
        companyRemoteDataSourceImpl: CompanyRemoteDataSourceImpl
    ): CompanyRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindsDirections5RemoteDataSource(
        directions5RemoteDataSourceImpl: Directions5RemoteDataSourceImpl
    ): Directions5RemoteDataSource

    @Singleton
    @Binds
    abstract fun bindsReservationRemoteDataSource(
        reservationRemoteSourceImpl: ReservationRemoteSourceImpl
    ): ReservationRemoteSource

}