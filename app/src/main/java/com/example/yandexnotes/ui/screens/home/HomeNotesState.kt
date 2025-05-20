package com.example.yandexnotes.ui.screens.home

import com.example.model.Note

sealed interface HomeNotesState {
    data class Success(val notes: List<Note> = listOf()) : HomeNotesState
    object Loading : HomeNotesState
    data class Error(val message: String) : HomeNotesState
}