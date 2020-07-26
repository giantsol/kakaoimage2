package com.ellen.kakaoimages.di

import com.ellen.kakaoimages.BuildConfig
import com.ellen.kakaoimages.BuildConfig.DEBUG
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    val connectTimeout: Long = 60
    val readTimeout: Long = 60

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
        if (DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        val apiKeyInterceptor = Interceptor { chain ->
            val builder = chain.request().newBuilder().apply {
                header("Authorization", BuildConfig.APP_KEY)
            }
            chain.proceed(builder.build())
        }
        okHttpClientBuilder.addInterceptor(apiKeyInterceptor)
        okHttpClientBuilder.build()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single {
        val baseUrl = BuildConfig.BASE_URL
        provideRetrofit(get(), baseUrl)
    }
}