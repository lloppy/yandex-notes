package com.example.data.repository

import android.content.Context
import com.example.domain.LocalRepository
import com.example.domain.NotesRepository
import com.example.domain.RemoteRepository
import com.example.model.Note
import com.example.model.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory

class FileNotebook(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) : NotesRepository {

    private val logger = LoggerFactory.getLogger(FileNotebook::class.java)
    override val notes: Flow<List<Note>> = localRepository.notes

    override suspend fun addNote(note: Note) {
        localRepository.addNote(note)
    }

    override fun getNoteByUid(uid: String): Flow<Note> = flow {
        localRepository.getNoteByUid(uid)?.let { emit(it) }
            ?: throw NoSuchElementException("Note not found")
    }

    override suspend fun updateNote(note: Note) {
        localRepository.updateNote(note)
    }

    override suspend fun deleteNote(uid: String) {
        localRepository.deleteNote(uid)
    }

    override suspend fun saveAllNotesToFile(context: Context) {
        localRepository.saveAllNotes()
    }

    override suspend fun loadAllNotesFromFile(context: Context) {
        localRepository.loadAllNotes()
    }


    // ---------- Реальные сетевые операции вместо заглушек ----------

    override suspend fun syncNoteToBackend(note: Note, deviceId: String) {
        when (val result = remoteRepository.updateNote(note, deviceId)) {
            is Result.Success -> {
                localRepository.updateNote(note)
                logger.info("Заметка успешно синхронизирована: ${note.title}")
            }

            is Result.Error -> {
                logger.error("Ошибка синхронизации заметки: ${result.error}")
                throw Exception("Network error: ${result.error}")
            }

            else -> {}
        }
    }

    override suspend fun saveNoteToBackend(note: Note, deviceId: String) {
        when (val result = remoteRepository.addNote(note, deviceId)) {
            is Result.Success -> {
                localRepository.addNote(note)
                logger.info("Заметка успешно сохранена на сервере: ${note.title}")
            }

            is Result.Error -> {
                logger.error("Ошибка сохранения заметки: ${result.error}")
                throw Exception("Network error: ${result.error}")
            }

            else -> {}
        }
    }

    override suspend fun deleteNoteFromBackend(uid: String) {
        when (remoteRepository.deleteNote(uid)) {
            is Result.Success -> {
                localRepository.deleteNote(uid)
                logger.info("Заметка успешно удалена с сервера UID=$uid")
            }

            is Result.Error -> {
                logger.error("Ошибка удаления заметки: $uid")
                throw Exception("Network error")
            }

            else -> {}
        }
    }

    override suspend fun fetchNotesFromBackend(): List<Note> {
        return when (val result = remoteRepository.getNotes()) {
            is Result.Success -> {
                result.data.forEach { note ->
                    if (localRepository.getNoteByUid(note.uid) == null) {
                        localRepository.addNote(note)
                    } else {
                        localRepository.updateNote(note)
                    }
                }
                logger.info("Успешно загружено ${result.data.size} заметок с сервера")
                result.data
            }

            is Result.Error -> {
                logger.error("Ошибка загрузки заметок с сервера")
                throw Exception("Сетевая ошибка: ${result.error}")
            }

            else -> {
                emptyList()
            }
        }
    }
}
