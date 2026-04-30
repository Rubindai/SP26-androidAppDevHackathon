package com.example.myapplication.di

import com.example.myapplication.data.remote.StudentApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlin.jvm.java



@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //TODO: Implement the OkHttpClient, Retrofit, and ApiService providers
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor (
                HttpLoggingInterceptor().apply {
                    level= HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json=Json{
            ignoreUnknownKeys=true
            isLenient=true
            coerceInputValues=true
        }
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://dummyjson.com/")
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipesApiService(retrofit: Retrofit): StudentApi{
        return retrofit.create(StudentApi::class.java)
    }

}
