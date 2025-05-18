package com.example.domain

import com.example.domain.util.DataError
import com.example.domain.util.EmptyResult
import com.example.model.Note
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun insertNote(note: Note): EmptyResult<DataError.Local>

    suspend fun deleteNote(note: Note)

    suspend fun deleteNoteById(id: Int)

    suspend fun deleteAllNotes()

    fun getNoteById(id: Int): Flow<Note?>

    fun getAllNotes(): Flow<List<Note>>

}
