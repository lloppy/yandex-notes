package com.example.data.remote.datasource

import com.example.data.remote.api.NotesApiService
import com.example.data.remote.mapper.toDto
import com.example.data.remote.mapper.toModel
import com.example.data.remote.model.PatchNotesRequest
import com.example.data.remote.model.SingleNoteRequest
import com.example.domain.RemoteDataSource
import com.example.domain.model.NoteUidModel
import com.example.domain.util.DataError
import com.example.domain.util.EmptyResult
import com.example.domain.util.Result
import com.example.model.Note

class NotesRemoteDataSource(
    private val api: NotesApiService,
    private val deviceId: String
) : RemoteDataSource {

    private var revision: Int = 0

    override suspend fun getNotes(): Result<List<Note>, DataError.Network> {
        return try {
            val response = api.getNotes()
            revision = response.revision
            Result.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun updateNote(note: Note): Result<NoteUidModel, DataError.Network> {
        return try {
            val response = api.updateNote(
                revision,
                note.uid,
                SingleNoteRequest(note.toDto(deviceId))
            )
            revision = response.revision
            Result.Success(NoteUidModel(response.element.id))
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun deleteNote(habitUid: String): EmptyResult<DataError.Network> {
        return try {
            val response = api.deleteNote(revision, habitUid)
            revision = response.revision
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    suspend fun patchNotes(notes: List<Note>): Result<List<Note>, DataError.Network> {
        return try {
            val response = api.patchNotes(
                revision,
                PatchNotesRequest(notes.map { it.toDto(deviceId) })
            )
            revision = response.revision
            Result.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}
