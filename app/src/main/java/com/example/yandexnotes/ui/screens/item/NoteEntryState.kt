package com.example.yandexnotes.ui.screens.item

data class NoteEntryState(
    val currentNote: NoteEntity = NoteEntity(),
    val isEntryValid: Boolean = false,
)