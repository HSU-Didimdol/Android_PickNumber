package com.example.picknumberproject.data.di

import android.content.Context
import androidx.room.Room
import com.example.picknumberproject.data.db.BankDao
import com.example.picknumberproject.data.db.BankDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideBankDB(@ApplicationContext context: Context): BankDatabase =
        Room.databaseBuilder(context, BankDatabase::class.java, "bank_table")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideBankDao(bankDatabase: BankDatabase): BankDao {
        return bankDatabase.getBankDao()
    }
}