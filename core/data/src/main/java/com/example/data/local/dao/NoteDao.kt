package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteEntity: NoteEntity)

    @Delete
    suspend fun delete(noteEntity: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM notes WHERE uid = :uid")
    suspend fun deleteByUid(uid: String)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getById(id: Int): Flow<NoteEntity>

    @Query("SELECT * FROM notes WHERE uid = :uid")
    fun getByUid(uid: String): Flow<NoteEntity>

    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<NoteEntity>>

}