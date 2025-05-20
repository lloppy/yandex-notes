package com.example.data.remote.datasource

import com.example.data.remote.api.NotesApiService
import com.example.data.remote.mapper.toDto
import com.example.data.remote.mapper.toModel
import com.example.data.remote.model.PatchNotesRequest
import com.example.data.remote.model.SingleNoteRequest
import com.example.domain.RemoteDataSource
import com.example.domain.model.NoteUidModel
import com.example.model.Note
import com.example.model.util.DataError.Network
import com.example.model.util.DataError.Network.CONFLICT
import com.example.model.util.DataError.Network.NO_INTERNET
import com.example.model.util.DataError.Network.PAYLOAD_TOO_LARGE
import com.example.model.util.DataError.Network.REQUEST_TIMEOUT
import com.example.model.util.DataError.Network.SERIALIZATION
import com.example.model.util.DataError.Network.SERVER_ERROR
import com.example.model.util.DataError.Network.TOO_MANY_REQUESTS
import com.example.model.util.DataError.Network.UNAUTHORIZED
import com.example.model.util.DataError.Network.UNKNOWN
import com.example.model.util.EmptyResult
import com.example.model.util.Result

class NotesRemoteDataSource(
    private val api: NotesApiService,
) : RemoteDataSource {

    private var revision: Int = 0

    override suspend fun getNotes(): Result<List<Note>, Network> {
        return try {
            val response = api.getNotes()

            revision = response.revision
            Result.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(Network.fromException(e))
        }
    }

    override suspend fun getNote(
        noteUid: String,
    ): Result<Note, Network> {
        return try {
            val response = api.getNote(
                noteUid = noteUid
            )
            revision = response.revision
            Result.Success(response.element.toModel())
        } catch (e: Exception) {
            Result.Error(Network.fromException(e))
        }
    }

    override suspend fun addNote(
        note: Note,
        deviceId: String,
    ): Result<NoteUidModel, Network> {
        return try {
            val response = api.addNote(
                revision = revision,
                request = SingleNoteRequest(note.toDto(deviceId))
            )
            revision = response.revision
            Result.Success(NoteUidModel(response.element.id))
        } catch (e: Exception) {
            Result.Error(Network.fromException(e))
        }
    }

    override suspend fun updateNote(
        note: Note,
        deviceId: String,
    ): Result<NoteUidModel, Network> {
        return try {
            val response = api.updateNote(
                revision = revision,
                noteUid = note.uid,
                request = SingleNoteRequest(note.toDto(deviceId))
            )
            revision = response.revision
            Result.Success(NoteUidModel(response.element.id))
        } catch (e: Exception) {
            Result.Error(Network.fromException(e))
        }
    }

    override suspend fun deleteNote(
        noteUid: String,
    ): EmptyResult<Network> {
        return try {
            val response = api.deleteNote(
                revision = revision,
                noteUid = noteUid
            )
            revision = response.revision
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Network.fromException(e))
        }
    }

    override suspend fun patchNotes(
        notes: List<Note>,
        deviceId: String,
    ): Result<List<Note>, Network> {
        return try {
            val response = api.patchNotes(
                revision = revision,
                request = PatchNotesRequest(notes.map { it.toDto(deviceId) })
            )
            revision = response.revision
            Result.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(Network.fromException(e))
        }
    }

    override suspend fun getNotesWithFailThreshold(
        generateFailsThreshold: Int?,
    ): Result<List<Note>, Network> {
        return try {
            val response = api.getNotes(
                generateFailsThreshold = generateFailsThreshold
            )
            revision = response.revision
            Result.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(Network.fromException(e))
        }
    }
}

private fun Network.Companion.fromException(e: Exception): Network {
    return when (e) {
        is java.net.SocketTimeoutException,
        is java.net.ConnectException,
            -> REQUEST_TIMEOUT

        is javax.net.ssl.SSLHandshakeException -> NO_INTERNET
        is java.io.InterruptedIOException -> REQUEST_TIMEOUT
        is java.net.UnknownHostException -> NO_INTERNET
        is retrofit2.HttpException -> when (e.code()) {
            401 -> UNAUTHORIZED
            408 -> REQUEST_TIMEOUT
            409 -> CONFLICT
            413 -> PAYLOAD_TOO_LARGE
            429 -> TOO_MANY_REQUESTS
            500, 502, 503, 504 -> SERVER_ERROR
            else -> UNKNOWN
        }

        is kotlinx.serialization.SerializationException -> SERIALIZATION

        is java.io.IOException -> NO_INTERNET
        else -> UNKNOWN
    }
}
