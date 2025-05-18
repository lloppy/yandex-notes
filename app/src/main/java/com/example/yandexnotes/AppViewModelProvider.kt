package com.example.yandexnotes

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
        initializer {
            val context = this.notesApplication().applicationContext
            val repository: NotesRepository = FileNotebook(context)
            HomeViewModel(
                repository = repository
            )
        }

        initializer {
            val context = this.notesApplication().applicationContext
            val repository: NotesRepository = FileNotebook(context)
            CreateNoteViewModel(
                repository = repository
            )
        }

        initializer {
            val context = this.notesApplication().applicationContext
            val repository: NotesRepository = FileNotebook(context)
            EditNoteViewModel(
                this.createSavedStateHandle(),
                repository = repository
            )
        }
    }
}

fun CreationExtras.notesApplication(): YandexNotesApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as YandexNotesApplication)