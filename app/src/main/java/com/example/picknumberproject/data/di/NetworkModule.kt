package com.example.picknumberproject.data.di

import androidx.viewbinding.BuildConfig
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

/**
 *  DB모듈은 앱이 구동 및 시작할때 하나의 객체로서 실행하게 됩니다.
 *  Activity Context가 아닌 Application Context로 구동하기 때문에, DB가 중간에 소멸되거나 소실되는 등의 문제를 방지하고
 *  조금 더 쉽게 알아보기 위해서 작성을 하였습니다.
 *  그래서 사용하고자하는 DB와 그와 관련된 DAO를 선언해주면 Provides와 SingleTon 객체로 생성이 됩니다.
 *  또한 db 패키지파일에 있는 추상클래스와 인터페이스를 여기서 사용합니다.
 */

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