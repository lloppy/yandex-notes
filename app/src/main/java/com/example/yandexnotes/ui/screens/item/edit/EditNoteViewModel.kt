package com.example.yandexnotes.ui.screens.item.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yandexnotes.data.NotesRepository
import com.example.yandexnotes.ui.screens.item.NoteEntity
import com.example.yandexnotes.ui.screens.item.create.NoteEntryState
import com.example.yandexnotes.ui.screens.item.toNote
import com.example.yandexnotes.ui.screens.item.toUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditNoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: NotesRepository
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


    fun updateItem() {
        if (validateInput()) {
            repository.updateNote(note = entryUiState.currentNote.toNote())
        }
    }
}
