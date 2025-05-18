package com.example.yandexnotes.ui.screens.item.create

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.yandexnotes.data.NotesRepository
import com.example.yandexnotes.ui.screens.item.NoteEntity
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveItem(context: Context) {
        if (validateInput()) {
            repository.addNote(note = entryUiState.currentNote.toNote())
        }
        saveToFile(context)
    }

    fun saveToFile(context: Context) {
        repository.saveToFile(context)
    }
}