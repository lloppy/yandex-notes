package com.example.yandexnotes.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yandexnotes.data.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: NotesRepository,
) : ViewModel() {
    val uiState: StateFlow<HomeNotesState> =
        repository.notes.map {
            HomeNotesState.Success(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = DELAY_FOR_KEEPING_INSTANCE_AFTER_CLOSING),
            initialValue = HomeNotesState.Loading
        )

    suspend fun deleteNoteById(noteUid: String) {
        repository.deleteNote(uid = noteUid)
    }

    fun loadFromFile(context: Context) {
        repository.loadFromFile(context)
    }


    companion object {
        const val DELAY_FOR_KEEPING_INSTANCE_AFTER_CLOSING = 3_000L
    }
}
