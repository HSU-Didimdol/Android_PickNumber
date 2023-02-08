package com.example.picknumberproject.api

import com.example.picknumberproject.BuildConfig
import com.example.picknumberproject.url.Url
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtil {

    val bankApi: BankApi by lazy { getBankRetrofit().create(BankApi::class.java) }
    val direction5Api: Direction5Api by lazy { getDirection5Retrofit().create(Direction5Api::class.java) }

    private fun getDirection5Retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.DIRECTION5_URL)
            .client(buildOkHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
    }

    private fun getBankRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.MOCK_BANK_URL)
            .client(buildOkHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
    }

    private fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    private fun buildOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

}