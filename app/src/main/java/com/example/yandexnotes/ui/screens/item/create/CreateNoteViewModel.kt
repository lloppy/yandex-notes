package com.example.yandexnotes.ui.screens.item.create

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.domain.NotesRepository
import com.example.yandexnotes.di.DeviceIdProvider
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

    suspend fun saveItem() {
        if (validateInput()) {
            repository.addNote(note = entryUiState.currentNote.toNote())
        }
    }


    suspend fun saveAndSyncItem(context: Context) {
        if (validateInput()) {
            repository.saveNoteToBackend(
                note = entryUiState.currentNote.toNote(),
                deviceId = DeviceIdProvider.getDeviceId(context)
            )

        }
    }
}