package com.example.data.local.datasource

import com.example.data.local.dao.NoteDao
import com.example.data.local.mappers.toDomain
import com.example.data.local.mappers.toEntity
import com.example.domain.LocalRepository
import com.example.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
    private val dao: NoteDao,
) : LocalRepository {

    override val notes: Flow<List<Note>> = dao.getAll().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun addNote(note: Note) {
        val updatedRows = dao.updateByUid(
            uid = note.uid,
            title = note.title,
            content = note.content,
            color = note.color,
            importance = note.importance,
            selfDestructDate = note.selfDestructDate
        )

        if (updatedRows == 0) {
            dao.insert(note.toEntity())
        }
    }

    override suspend fun updateNote(note: Note) {
        val updatedRows = dao.updateByUid(
            uid = note.uid,
            title = note.title,
            content = note.content,
            color = note.color,
            importance = note.importance,
            selfDestructDate = note.selfDestructDate
        )
        if (updatedRows == 0) {
            dao.insert(note.toEntity())
        }
    }

    override suspend fun getNoteByUid(uid: String): Note? {
        return dao.getByUid(uid).firstOrNull()?.toDomain()
    }

    override suspend fun deleteNote(uid: String) {
        dao.deleteByUid(uid)
    }


    // ---------- unused ----------

    override suspend fun saveAllNotes() {
        // Для Room не требуется отдельное сохранение
    }

    override suspend fun loadAllNotes() {
        // Данные уже загружаются автоматически через Flow
    }

    override suspend fun saveNoteToCache(note: Note) {
        // Для Room кэш не требуется
    }

    override suspend fun loadNoteFromCache(uid: String): Note? {
        // Для Room кэш не требуется
        return null
    }

    override suspend fun deleteNoteFromCache(uid: String) {
        // Для Room кэш не требуется
    }
}
