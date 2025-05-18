package com.example.yandexnotes.ui.screens.item.create

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.domain.NotesRepository
import com.example.yandexnotes.ui.screens.item.NoteEntity
import com.example.yandexnotes.ui.screens.item.NoteEntryState
import com.example.yandexnotes.ui.screens.item.toNote

class CreateNoteViewModel(
    private val repository: NotesRepository,
) : ViewModel() {

    var entryUiState by mutableStateOf(NoteEntryState())
        private set

    fun updateUiState(newNote: NoteEntity) {
        entryUiState = NoteEntryState(
            currentNote = newNote,
            isEntryValid = validateInput(newNote)
        )
    }

    private fun validateInput(uiState: NoteEntity = entryUiState.currentNote): Boolean {
        return with(uiState) {
            title.isNotBlank() && content.isNotBlank()
        }
    }

    fun saveItem(context: Context) {
        if (validateInput()) {
            repository.addNote(note = entryUiState.currentNote.toNote())
        }
        saveToFile(context)
    }

    private fun saveToFile(context: Context) {
        repository.saveAllNotesToFile(context)
    }
}