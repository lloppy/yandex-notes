package com.example.yandexnotes

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.yandexnotes.data.FileNotebook
import com.example.yandexnotes.data.NotesRepository
import com.example.yandexnotes.ui.screens.home.HomeViewModel
import com.example.yandexnotes.ui.screens.item.create.CreateNoteViewModel
import com.example.yandexnotes.ui.screens.item.edit.EditNoteViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        val repository: NotesRepository = FileNotebook()

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