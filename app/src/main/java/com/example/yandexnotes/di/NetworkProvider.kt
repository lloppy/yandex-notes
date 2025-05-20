package com.example.yandexnotes.di

import com.example.data.remote.api.NotesApiService
import okhttp3.OkHttpClient
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object NetworkProvider {
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            })
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $AUTH_TOKEN")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")

                originalRequest.header("X-Generate-Fails")?.let {
                    requestBuilder.header("X-Generate-Fails", it)
                }

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val notesApiService by lazy {
        retrofit.create(NotesApiService::class.java)
    }

    private const val API_BASE_URL = "https://beta.mrdekk.ru/todo/"
    private const val AUTH_TOKEN = "9a303c11-a066-484d-8891-743755d711e9"
}