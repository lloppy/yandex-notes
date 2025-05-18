package com.example.yandexnotes.ui.screens.item.create

import com.example.yandexnotes.ui.screens.item.NoteEntity

data class NoteEntryState(
    val currentNote: NoteEntity = NoteEntity(),
    val isEntryValid: Boolean = false,
)