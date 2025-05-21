package com.example.data.remote.datasource

import com.example.data.remote.api.NotesApiService
import com.example.data.remote.mapper.toDto
import com.example.data.remote.mapper.toModel
import com.example.data.remote.model.PatchNotesRequest
import com.example.data.remote.model.SingleNoteRequest
import com.example.domain.RemoteRepository
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
import org.slf4j.LoggerFactory

class RemoteRepositoryImpl(
    private val api: NotesApiService,
) : RemoteRepository {

    private val logger = LoggerFactory.getLogger(RemoteRepositoryImpl::class.java)
    private var revision: Int = 0

    override suspend fun getNotes(): Result<List<Note>, Network> {
        return try {
            logger.debug("Fetching notes from backend")
            val response = api.getNotes()
            revision = response.revision
            logger.info("Successfully fetched ${response.list.size} notes, new revision: $revision")
            Result.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            val error = Network.fromException(e)
            logger.error("Failed to fetch notes: ${error.name}", e)
            Result.Error(error)
        }
    }

    override suspend fun getNote(noteUid: String): Result<Note, Network> {
        return try {
            logger.debug("Fetching note with UID: $noteUid")
            val response = api.getNote(noteUid = noteUid)
            revision = response.revision
            logger.info("Successfully fetched note ${response.element.id}, new revision: $revision")
            Result.Success(response.element.toModel())
        } catch (e: Exception) {
            val error = Network.fromException(e)
            logger.error("Failed to fetch note $noteUid: ${error.name}", e)
            Result.Error(error)
        }
    }

    override suspend fun addNote(note: Note, deviceId: String): Result<NoteUidModel, Network> {
        return try {
            val currentRevision = api.getNotes().revision
            logger.debug("Adding new note: ${note.title} (${note.uid})")

            val response = api.addNote(
                revision = currentRevision,
                request = SingleNoteRequest(note.toDto(deviceId))
            )

            revision = response.revision
            logger.info("Successfully added note ${response.element.id}, new revision: $revision")
            Result.Success(NoteUidModel(response.element.id))
        } catch (e: Exception) {
            val error = Network.fromException(e)
            logger.error("Failed to add note ${note.uid}: ${error.name}", e)
            Result.Error(error)
        }
    }

    override suspend fun updateNote(note: Note, deviceId: String): Result<NoteUidModel, Network> {
        return try {
            val currentRevision = api.getNotes().revision
            logger.debug("Updating note: ${note.title} (${note.uid})")

            try {
                val response = api.updateNote(
                    revision = currentRevision,
                    noteUid = note.uid,
                    request = SingleNoteRequest(note.toDto(deviceId))
                )

                revision = response.revision
                logger.info("Successfully updated note ${response.element.id}, new revision: $revision")
                Result.Success(NoteUidModel(response.element.id))
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 404) {
                    logger.warn("Note not found (404), trying to add as new")
                    addNote(note, deviceId)
                } else {
                    throw e
                }
            }
        } catch (e: Exception) {
            val error = Network.fromException(e)
            logger.error("Failed to update note ${note.uid}: ${error.name}", e)
            Result.Error(error)
        }
    }

    override suspend fun deleteNote(noteUid: String): EmptyResult<Network> {
        return try {
            logger.debug("Deleting note with UID: $noteUid")
            val currentRevision = api.getNotes().revision
            val response = api.deleteNote(revision = currentRevision, noteUid = noteUid)
            revision = response.revision
            logger.info("Successfully deleted note $noteUid, new revision: $revision")
            Result.Success(Unit)
        } catch (e: Exception) {
            val error = Network.fromException(e)
            logger.error("Failed to delete note $noteUid: ${error.name}", e)
            Result.Error(error)
        }
    }

    override suspend fun patchNotes(
        notes: List<Note>,
        deviceId: String,
    ): Result<List<Note>, Network> {
        return try {
            logger.debug("Patching ${notes.size} notes")
            val currentRevision = api.getNotes().revision
            val response = api.patchNotes(
                revision = currentRevision,
                request = PatchNotesRequest(notes.map { it.toDto(deviceId) })
            )
            revision = response.revision
            logger.info("Successfully patched notes, new revision: $revision")
            Result.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            val error = Network.fromException(e)
            logger.error("Failed to patch notes: ${error.name}", e)
            Result.Error(error)
        }
    }

    override suspend fun getNotesWithFailThreshold(generateFailsThreshold: Int?): Result<List<Note>, Network> {
        return try {
            logger.debug("Fetching notes with fail threshold: $generateFailsThreshold")
            val response = api.getNotes(generateFailsThreshold = generateFailsThreshold)
            revision = response.revision
            logger.info("Successfully fetched ${response.list.size} notes with threshold, new revision: $revision")
            Result.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            val error = Network.fromException(e)
            logger.error("Failed to fetch notes with threshold: ${error.name}", e)
            Result.Error(error)
        }
    }

    override suspend fun clearAllNotes() {
        try {
            var currentRevision = api.getNotes().revision

            api.getNotes().list.forEach { note ->
                val response = api.deleteNote(
                    revision = currentRevision,
                    noteUid = note.id
                )
                currentRevision = response.revision
            }
        } catch (e: Exception) {
            logger.error("Failed to clear notes", e)
        }
    }

    private fun Network.Companion.fromException(e: Exception): Network {
        return when (e) {
            is java.net.SocketTimeoutException,
            is java.net.ConnectException,
                -> {
                logger.warn("Network timeout/connection error", e)
                REQUEST_TIMEOUT
            }

            is javax.net.ssl.SSLHandshakeException -> {
                logger.warn("SSL handshake error", e)
                NO_INTERNET
            }

            is java.io.InterruptedIOException -> {
                logger.warn("Request interrupted", e)
                REQUEST_TIMEOUT
            }

            is java.net.UnknownHostException -> {
                logger.warn("Unknown host/no internet", e)
                NO_INTERNET
            }

            is retrofit2.HttpException -> {
                val error = when (e.code()) {
                    400 -> {
                        logger.error(
                            "Bad Request (400) - likely invalid request data: ${
                                e.response()?.errorBody()?.string()
                            }"
                        )
                        Network.BAD_REQUEST
                    }

                    401 -> UNAUTHORIZED
                    408 -> REQUEST_TIMEOUT
                    409 -> CONFLICT
                    413 -> PAYLOAD_TOO_LARGE
                    429 -> TOO_MANY_REQUESTS
                    500, 502, 503, 504 -> SERVER_ERROR
                    else -> UNKNOWN
                }
                logger.warn("HTTP error ${e.code()}: ${error.name}", e)
                error
            }

            is kotlinx.serialization.SerializationException -> {
                logger.error("Serialization error", e)
                SERIALIZATION
            }

            is java.io.IOException -> {
                logger.warn("IO/Network error", e)
                NO_INTERNET
            }

            else -> {
                logger.error("Unknown network error", e)
                UNKNOWN
            }
        }
    }
}