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

    // all notes
    fun saveAllNotesToFile(context: Context)
    fun loadAllNotesFromFile(context: Context)

    // cache
    fun saveNoteToCache(note: Note, context: Context)
    fun loadNoteFromCache(uid: String, context: Context): Note?
    fun deleteNoteFromCache(uid: String, context: Context)

    // backend
    suspend fun saveNoteToBackend(note: Note, deviceId: String)
    suspend fun syncNoteToBackend(note: Note, deviceId: String)
    suspend fun deleteNoteFromBackend(uid: String)
    suspend fun fetchNotesFromBackend(): List<Note>
}