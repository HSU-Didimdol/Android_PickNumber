package com.example.picknumberproject.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.picknumberproject.data.db.CompanyDao
import com.example.picknumberproject.data.db.CompanyDatabase
import com.example.picknumberproject.data.db.UserDao
import com.example.picknumberproject.data.db.UserDatabase
import com.example.picknumberproject.data.source.AuthLocalDataSource
import com.example.picknumberproject.data.source.AuthLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 *  DB모듈은 앱이 구동 및 시작할때 하나의 객체로서 실행하게 됩니다.
 *  Activity Context가 아닌 Application Context로 구동하기 때문에, DB가 중간에 소멸되거나 소실되는 등의 문제를 방지하고
 *  조금 더 쉽게 알아보기 위해서 작성을 하였습니다.
 *  그래서 사용하고자하는 DB와 그와 관련된 DAO를 선언해주면 Provides와 SingleTon 객체로 생성이 됩니다.
 *  또한 db 패키지파일에 있는 추상클래스와 인터페이스를 여기서 사용합니다.
 */

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
        return companyDatabase.getDao()
    }

    @Provides
    @Singleton
    fun provideUserDB(@ApplicationContext context: Context): UserDatabase =
        Room.databaseBuilder(context, UserDatabase::class.java, "user_table")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.getDao()
    }

    @Singleton
    @Provides
    @Named("auth")
    fun provideEncryptedSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "auth",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideAuthLocalDataSource(
        @Named("auth") sharedPreferences: SharedPreferences
    ): AuthLocalDataSource {
        return AuthLocalDataSourceImpl(sharedPreferences)
    }
}
