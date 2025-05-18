package com.example.domain

import com.example.domain.model.NoteUidModel
import com.example.domain.util.DataError
import com.example.domain.util.EmptyResult
import com.example.domain.util.Result
import com.example.model.Note

interface RemoteDataSource {

    suspend fun getNotes(): Result<List<Note>, DataError.Network>

    suspend fun updateNote(habit: Note): Result<NoteUidModel, DataError.Network>

    suspend fun deleteNote(habitUid: String): EmptyResult<DataError.Network>

}