package com.example.yandexnotes.di

import android.content.Context
import com.example.data.local.OfflineDatabase
import com.example.data.local.datasource.RoomRepositoryImpl
import com.example.data.remote.datasource.RemoteRepositoryImpl
import com.example.data.NotesRepositoryImpl
import com.example.data.remote.api.NotesApiService
import com.example.domain.LocalRepository
import com.example.domain.NotesRepository
import com.example.domain.RemoteRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val localRepository: LocalRepository
    val remoteRepository: RemoteRepository

    val notesRepository: NotesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val remoteRepository: RemoteRepository by lazy {
        RemoteRepositoryImpl(api = Network.notesApiService)
    }

    // override val jsonRepository: LocalRepository = JsonRepositoryImpl(context = context)          // use this if u wanna save into json
    override val localRepository: LocalRepository by lazy {
        RoomRepositoryImpl(
            dao = OfflineDatabase.getDatabase(context).noteDao()
        )
    }

    override val notesRepository: NotesRepository = NotesRepositoryImpl(
        remoteRepository = remoteRepository,
        localRepository = localRepository
    )
}