package com.example.domain

import com.example.domain.model.NoteUidModel
import com.example.model.Note
import com.example.model.util.DataError
import com.example.model.util.EmptyResult
import com.example.model.util.Result

interface RemoteRepository {

    suspend fun getNotes(): Result<List<Note>, DataError.Network>

    suspend fun getNote(
        noteUid: String
    ): Result<Note, DataError.Network>

    suspend fun addNote(
        note: Note,
        deviceId: String
    ): Result<NoteUidModel, DataError.Network>

    suspend fun updateNote(
        note: Note,
        deviceId: String,
    ): Result<NoteUidModel, DataError.Network>

    suspend fun deleteNote(
        noteUid: String
    ): EmptyResult<DataError.Network>

    suspend fun patchNotes(
        notes: List<Note>,
        deviceId: String,
    ): Result<List<Note>, DataError.Network>

    suspend fun getNotesWithFailThreshold(
        generateFailsThreshold: Int?
    ): Result<List<Note>, DataError.Network>

    suspend fun clearAllNotes()
}