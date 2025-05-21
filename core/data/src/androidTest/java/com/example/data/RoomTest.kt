package com.example.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import android.content.Context
import android.graphics.Color
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.OfflineDatabase
import com.example.data.local.dao.NoteDao
import com.example.data.local.datasource.RoomRepositoryImpl
import com.example.data.local.mappers.toDomain
import com.example.data.local.mappers.toEntity
import com.example.model.Note
import com.example.model.Importance
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class RoomRepositoryImplTest {

    private lateinit var noteDao: NoteDao
    private lateinit var db: OfflineDatabase
    private lateinit var repository: RoomRepositoryImpl

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, OfflineDatabase::class.java)
            .addTypeConverter(com.example.data.local.mappers.Converters())
            .allowMainThreadQueries()
            .build()
        noteDao = db.noteDao()
        repository = RoomRepositoryImpl(noteDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private val note1 = TestData.testNotes[0]
    private val note2 = TestData.testNotes[1]

    @Test
    fun testMappers() {
        val note = note1
        val entity = note.toEntity()
        val mappedBack = entity.toDomain()

        assertEquals(note, mappedBack)
    }

    @Test
    @Throws(Exception::class)
    fun addNote_shouldInsertNewNote() = runBlocking {
        repository.addNote(note1)
        val allNotes = repository.notes.first()
        assertEquals(1, allNotes.size)
        assertEquals(note1, allNotes[0])
    }

    @Test
    @Throws(Exception::class)
    fun addNote_withDuplicateUid_shouldUpdateNote() = runBlocking {
        repository.addNote(note1)
        val updatedNote = note1.copy(title = "Updated title")
        repository.addNote(updatedNote)

        val allNotes = repository.notes.first()
        assertEquals(1, allNotes.size)
        assertEquals(updatedNote, allNotes[0])
    }

    @Test
    @Throws(Exception::class)
    fun updateNote_shouldUpdateExistingNote() = runBlocking {
        repository.addNote(note1)
        val updatedNote = note1.copy(title = "Updated title")
        repository.updateNote(updatedNote)

        val allNotes = repository.notes.first()
        assertEquals(1, allNotes.size)
        assertEquals(updatedNote, allNotes[0])
    }

    @Test
    @Throws(Exception::class)
    fun updateNote_nonExisting_shouldInsertNewNote() = runBlocking {
        repository.updateNote(note1)
        val allNotes = repository.notes.first()
        assertEquals(1, allNotes.size)
        assertEquals(note1, allNotes[0])
    }

    @Test
    @Throws(Exception::class)
    fun getNoteByUid_shouldReturnCorrectNote() = runBlocking {
        repository.addNote(note1)
        repository.addNote(note2)

        val retrievedNote = repository.getNoteByUid(note1.uid)
        assertEquals(note1, retrievedNote)
    }

    @Test(expected = NoSuchElementException::class)
    @Throws(Exception::class)
    fun getNoteByUid_nonExisting_shouldThrow() {
        runBlocking {
            repository.getNoteByUid("non-existing-uid")
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteNote_shouldRemoveNote() = runBlocking {
        repository.addNote(note1)
        repository.addNote(note2)

        repository.deleteNote(note1.uid)

        val allNotes = repository.notes.first()
        assertEquals(1, allNotes.size)
        assertEquals(note2, allNotes[0])
    }

    @Test
    @Throws(Exception::class)
    fun notesFlow_shouldEmitAllNotes() = runBlocking {
        repository.addNote(note1)
        repository.addNote(note2)

        val allNotes = repository.notes.first()
        assertEquals(2, allNotes.size)
        assertTrue(allNotes.contains(note1))
        assertTrue(allNotes.contains(note2))
    }

    @Test
    @Throws(Exception::class)
    fun unusedMethods_shouldDoNothing() = runBlocking {
        repository.saveAllNotes()
        repository.loadAllNotes()
        repository.saveNoteToCache(note1)
        repository.deleteNoteFromCache(note1.uid)

        val cachedNote = repository.loadNoteFromCache(note1.uid)
        assertEquals(null, cachedNote)
    }
}

object TestData {
    val testNotes = listOf(
        Note(
            uid = "uid1",
            title = "Note 1",
            content = "Content 1",
            color = Color.RED,
            importance = Importance.HIGH,
            selfDestructDate = LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC)
        ),
        Note(
            uid = "uid2",
            title = "Note 2",
            content = "Content 2",
            color = Color.BLUE,
            importance = Importance.NORMAL,
            selfDestructDate = LocalDateTime.now().plusDays(2).toEpochSecond(ZoneOffset.UTC)
        )
    )
}