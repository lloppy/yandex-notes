package com.example.domain

import android.content.Context
import com.example.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    val notes: Flow<List<Note>>
    fun getNoteByUid(uid: String): Flow<Note>

    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(uid: String)


    // all notes to json
    suspend fun saveAllNotesToFile(context: Context)
    suspend fun loadAllNotesFromFile(context: Context)

    // backend
    suspend fun saveNoteToBackend(note: Note, deviceId: String)
    suspend fun syncNoteToBackend(note: Note, deviceId: String)
    suspend fun deleteNoteFromBackend(uid: String)
    suspend fun fetchNotesFromBackend(): List<Note>
    suspend fun deleteAllNotesFromServer()
}