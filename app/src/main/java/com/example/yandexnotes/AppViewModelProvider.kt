package com.example.yandexnotes

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.data.repository.FileNotebook
import com.example.data.remote.datasource.NotesRemoteDataSource
import com.example.domain.NotesRepository
import com.example.domain.RemoteDataSource
import com.example.yandexnotes.di.NetworkProvider
import com.example.yandexnotes.ui.screens.home.HomeViewModel
import com.example.yandexnotes.ui.screens.item.create.CreateNoteViewModel
import com.example.yandexnotes.ui.screens.item.edit.EditNoteViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        val dataSource: RemoteDataSource = NotesRemoteDataSource(api = NetworkProvider.notesApiService)
        val repository: NotesRepository = FileNotebook(remoteDataSource = dataSource)

        initializer {
            HomeViewModel(
                repository = repository
            )
        }

        initializer {
            CreateNoteViewModel(
                repository = repository
            )
        }

        initializer {
            EditNoteViewModel(
                this.createSavedStateHandle(),
                repository = repository
            )
        }
    }
}

fun CreationExtras.notesApplication(): YandexNotesApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as YandexNotesApplication)