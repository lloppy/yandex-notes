package com.example.yandexnotes.ui.screens.item.edit

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.NotesRepository
import com.example.yandexnotes.di.DeviceIdProvider
import com.example.yandexnotes.ui.screens.item.NoteEntity
import com.example.yandexnotes.ui.screens.item.NoteEntryState
import com.example.yandexnotes.ui.screens.item.toNote
import com.example.yandexnotes.ui.screens.item.toUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditNoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: NotesRepository,
) : ViewModel() {
    private val noteUid: String = checkNotNull(savedStateHandle[EditNoteDestination.itemIdArg])

    var entryUiState by mutableStateOf(NoteEntryState())
        private set

    fun updateUiState(newNote: NoteEntity) {
        entryUiState = NoteEntryState(
            currentNote = newNote,
            isEntryValid = validateInput(newNote)
        )
    }

    init {
        viewModelScope.launch {
            entryUiState = repository.getNoteByUid(noteUid)
                .filterNotNull()
                .map {
                    NoteEntryState(
                        currentNote = it.toUiState(),
                        isEntryValid = true
                    )
                }
                .first()
        }
    }

    private fun validateInput(uiState: NoteEntity = entryUiState.currentNote): Boolean =
        with(uiState) {
            title.isNotBlank() && content.isNotBlank()
        }


    suspend fun updateItem() {
        if (validateInput()) {
            repository.updateNote(note = entryUiState.currentNote.toNote())
        }
    }

    suspend fun updateAndSyncItem(context: Context) {
        if (validateInput()) {
            repository.syncNoteToBackend(
                note = entryUiState.currentNote.toNote(),
                deviceId = DeviceIdProvider.getDeviceId(context)
            )
        }
    }
}
