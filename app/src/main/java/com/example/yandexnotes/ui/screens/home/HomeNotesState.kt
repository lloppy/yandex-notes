package com.example.yandexnotes.ui.screens.home

import com.example.yandexnotes.model.Note

sealed interface HomeNotesState {
    data class Success(val notes: List<Note> = listOf()) : HomeNotesState
    object Loading : HomeNotesState
}