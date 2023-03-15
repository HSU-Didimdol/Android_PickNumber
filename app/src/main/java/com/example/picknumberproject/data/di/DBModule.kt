package com.example.picknumberproject.data.di

import android.content.Context
import androidx.room.Room
import com.example.picknumberproject.data.db.CompanyDao
import com.example.picknumberproject.data.db.CompanyDatabase
import com.example.picknumberproject.data.db.UserDatabase
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
    fun provideCompanyDB(@ApplicationContext context: Context): CompanyDatabase =
        Room.databaseBuilder(context, CompanyDatabase::class.java, "company_table")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideCompanyDao(companyDatabase: CompanyDatabase): CompanyDao {
        return companyDatabase.getBankDao()
    }
}