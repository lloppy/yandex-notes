package com.example.domain

import com.example.model.util.DataError
import com.example.model.util.EmptyResult
import com.example.model.Note
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun insertNote(note: Note): EmptyResult<DataError.Local>

    suspend fun deleteNoteById(id: Int)

    suspend fun deleteAllNotes()

    fun getNoteById(id: Int): Flow<Note?>

    fun getAllNotes(): Flow<List<Note>>

}
