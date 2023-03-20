package com.example.picknumberproject.data.di

import com.example.picknumberproject.BuildConfig
import com.example.picknumberproject.data.api.CompanyApi
import com.example.picknumberproject.data.api.Direction5Api
import com.example.picknumberproject.data.api.MainServerApi
import com.example.picknumberproject.data.di.annotation.CompanyRetrofitInstance
import com.example.picknumberproject.data.di.annotation.Directions5RetrofitInstance
import com.example.picknumberproject.data.di.annotation.ServerRetrofitInstance
import com.example.picknumberproject.data.url.Url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }


    @ServerRetrofitInstance
    @Provides
    @Singleton
    fun provideServerRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @CompanyRetrofitInstance
    @Provides
    @Singleton
    fun provideCompanyRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.COMPANY_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Directions5RetrofitInstance
    @Provides
    @Singleton
    fun provideDirections5Retrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.DIRECTION5_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideCompanyApiService(@CompanyRetrofitInstance retrofit: Retrofit): CompanyApi {
        return retrofit.create(CompanyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideServerApiServer(@ServerRetrofitInstance retrofit: Retrofit): MainServerApi {
        return retrofit.create(MainServerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDirection5ApiService(@Directions5RetrofitInstance retrofit: Retrofit): Direction5Api {
        return retrofit.create(Direction5Api::class.java)
    }
}