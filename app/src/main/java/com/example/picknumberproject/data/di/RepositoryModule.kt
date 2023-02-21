package com.example.picknumberproject.data.di

import com.example.picknumberproject.data.repository.BankRepositoryImpl
import com.example.picknumberproject.domain.repository.BankRepository
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
    abstract fun bindBankRepository(
        bankRepositoryImpl: BankRepositoryImpl
    ): BankRepository

}