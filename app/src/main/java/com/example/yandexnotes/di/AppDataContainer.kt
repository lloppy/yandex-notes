package com.example.yandexnotes.di

import android.content.Context
import com.example.data.local.OfflineDatabase
import com.example.data.local.datasource.RoomRepositoryImpl
import com.example.data.remote.datasource.RemoteRepositoryImpl
import com.example.data.repository.FileNotebook
import com.example.domain.LocalRepository
import com.example.domain.NotesRepository
import com.example.domain.RemoteRepository

interface AppContainer {
    val localRepository: LocalRepository
    val remoteRepository: RemoteRepository

    val notesRepository: NotesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val remoteRepository: RemoteRepository =
        RemoteRepositoryImpl(api = NetworkProvider.notesApiService)

    // override val jsonRepository: LocalRepository = JsonRepositoryImpl(context = context)          // use this if u wanna save into json
    override val localRepository: LocalRepository by lazy {
        RoomRepositoryImpl(
            dao = OfflineDatabase.getDatabase(context).noteDao()
        )
    }

    override val notesRepository: NotesRepository = FileNotebook(
        remoteRepository = remoteRepository,
        localRepository = localRepository
    )
}