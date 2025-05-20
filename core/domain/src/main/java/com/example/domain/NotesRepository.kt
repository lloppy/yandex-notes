package com.example.domain

import android.content.Context
import com.example.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    val notes: Flow<List<Note>>

    // local, without saving
    fun addNote(note: Note)
    fun getNoteByUid(uid: String): Flow<Note>
    fun updateNote(note: Note)
    fun deleteNote(uid: String)

    // all notes to json
    fun saveAllNotesToFile(context: Context)
    fun loadAllNotesFromFile(context: Context)

    // backend
    suspend fun saveNoteToBackend(note: Note, deviceId: String)
    suspend fun syncNoteToBackend(note: Note, deviceId: String)
    suspend fun deleteNoteFromBackend(uid: String)
    suspend fun fetchNotesFromBackend(): List<Note>
}