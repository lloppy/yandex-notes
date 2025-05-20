package com.example.domain

import com.example.model.Note
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    // Основные операции с заметками
    val notes: Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun getNoteByUid(uid: String): Note?
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(uid: String)

    // Синхронизация/сохранение состояния
    suspend fun saveAllNotes()
    suspend fun loadAllNotes()

    // Опциональные методы для кэширования
    suspend fun saveNoteToCache(note: Note)
    suspend fun loadNoteFromCache(uid: String): Note?
    suspend fun deleteNoteFromCache(uid: String)
}