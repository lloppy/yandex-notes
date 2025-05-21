package com.example.yandexnotes.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.LocalRepository
import com.example.domain.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: NotesRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    val uiState: StateFlow<HomeNotesState> =
        localRepository.notes.map {
            HomeNotesState.Success(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = DELAY_FOR_KEEPING_INSTANCE_AFTER_CLOSING),
            initialValue = HomeNotesState.Loading
        )

    suspend fun deleteNoteById(noteUid: String) {
        repository.deleteNote(uid = noteUid)
    }

    suspend fun deleteNoteFromServer(noteUid: String) {
        repository.deleteNoteFromBackend(uid = noteUid)
    }

    suspend fun syncFromServer() {
        repository.fetchNotesFromBackend()
    }

    suspend fun deleteAllFromServer() {
        repository.deleteAllNotesFromServer()
    }

    companion object {
        const val DELAY_FOR_KEEPING_INSTANCE_AFTER_CLOSING = 3_000L
    }
}
