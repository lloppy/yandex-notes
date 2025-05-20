package com.example.yandexnotes.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.yandexnotes.YandexNotesApplication
import com.example.yandexnotes.ui.screens.home.HomeViewModel
import com.example.yandexnotes.ui.screens.item.create.CreateNoteViewModel
import com.example.yandexnotes.ui.screens.item.edit.EditNoteViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            HomeViewModel(
                repository = notesApplication().container.notesRepository,
                localRepository = notesApplication().container.localRepository
            )
        }

        initializer {
            CreateNoteViewModel(
                repository = notesApplication().container.notesRepository,
            )
        }

        initializer {
            EditNoteViewModel(
                this.createSavedStateHandle(),
                repository = notesApplication().container.notesRepository,
            )
        }
    }
}

fun CreationExtras.notesApplication(): YandexNotesApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as YandexNotesApplication)