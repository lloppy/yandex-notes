package com.example.data.local.datasource

import android.content.Context
import com.example.domain.LocalRepository
import com.example.model.Note
import com.example.model.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class JsonRepositoryImpl(
    private val context: Context
) : LocalRepository {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    override val notes: Flow<List<Note>> = _notes

    override suspend fun addNote(note: Note) {
        _notes.value += note
    }

    override suspend fun getNoteByUid(uid: String): Note? {
        return _notes.value.find { it.uid == uid }
    }

    override suspend fun updateNote(note: Note) {
        _notes.value = _notes.value.map { if (it.uid == note.uid) note else it }
    }

    override suspend fun deleteNote(uid: String) {
        _notes.value = _notes.value.filterNot { it.uid == uid }
    }

    override suspend fun saveAllNotes() {
        val file = File(context.filesDir, "notes.json")
        val jsonArray = JSONArray()
        _notes.value.forEach { jsonArray.put(it.json) }
        file.writeText(jsonArray.toString())
    }

    override suspend fun loadAllNotes() {
        val file = File(context.filesDir, "notes.json")
        if (!file.exists()) return

        val jsonText = file.readText()
        val array = JSONArray(jsonText)
        _notes.value = (0 until array.length()).mapNotNull {
            Note.parse(array.getJSONObject(it))
        }
    }

    override suspend fun saveNoteToCache(note: Note) {
        File(context.filesDir, "note_${note.uid}.json").writeText(note.json.toString())
    }

    override suspend fun loadNoteFromCache(uid: String): Note? {
        val file = File(context.filesDir, "note_$uid.json")
        return if (file.exists()) Note.parse(JSONObject(file.readText())) else null
    }

    override suspend fun deleteNoteFromCache(uid: String) {
        File(context.filesDir, "note_$uid.json").delete()
    }
}