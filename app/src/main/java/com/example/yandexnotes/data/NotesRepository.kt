package com.example.yandexnotes.data

import android.content.Context
import com.example.yandexnotes.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    val notes: Flow<List<Note>>

    fun addNote(note: Note)
    fun getNoteByUid(uid: String): Flow<Note>
    fun updateNote(note: Note)
    fun deleteNote(uid: String)

    fun saveToFile(context: Context)
    fun loadFromFile(context: Context)

    fun saveNoteToCache(note: Note, context: Context)
    fun loadNoteFromCache(uid: String, context: Context): Note?
    fun deleteNoteFromCache(uid: String, context: Context)

    suspend fun syncNoteToBackend(note: Note)
    suspend fun deleteNoteFromBackend(uid: String)
    suspend fun fetchNotesFromBackend(): List<Note>
}