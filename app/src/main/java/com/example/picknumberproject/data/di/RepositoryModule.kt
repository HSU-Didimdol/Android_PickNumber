package com.example.picknumberproject.data.di

import com.example.picknumberproject.data.repository.CompanyRepositoryImpl
import com.example.picknumberproject.data.repository.ReservationRepositoryImpl
import com.example.picknumberproject.domain.repository.CompanyRepository
import com.example.picknumberproject.domain.repository.ReservationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCompanyRepository(
        companyRepositoryImpl: CompanyRepositoryImpl
    ): CompanyRepository

    @Singleton
    @Binds
    abstract fun bindReservationRepository(
        reservationRepositoryImpl: ReservationRepositoryImpl
    ) : ReservationRepository

}