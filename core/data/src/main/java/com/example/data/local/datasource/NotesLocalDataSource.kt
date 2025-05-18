package com.example.data.local.datasource

import android.database.sqlite.SQLiteFullException
import com.example.data.local.dao.NoteDao
import com.example.data.local.mappers.toDomain
import com.example.data.local.mappers.toEntity
import com.example.domain.LocalDataSource
import com.example.domain.util.DataError
import com.example.domain.util.EmptyResult
import com.example.domain.util.Result
import com.example.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesLocalDataSource(
    private val dao: NoteDao,
) : LocalDataSource {

    override suspend fun insertNote(note: Note): EmptyResult<DataError.Local> {
        return try {
            dao.insert(
                noteEntity = note.toEntity()
            )
            Result.Success(Unit)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteNoteById(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun deleteAllNotes() {
        dao.deleteAll()
    }

    override fun getNoteById(id: Int): Flow<Note?> {
        return dao.getById(id = id)
            .map {
                it.toDomain()
            }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAll()
            .map { entityList ->
                entityList.map {
                    it.toDomain()
                }
            }
    }

}
