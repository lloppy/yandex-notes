package com.example.data.cache

import android.content.Context
import com.example.model.Note
import com.example.model.Note.Companion
import com.example.model.mapper.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.slf4j.LoggerFactory
import java.io.File

class FileNotebook : com.example.domain.NotesRepository {

    private val logger = LoggerFactory.getLogger(FileNotebook::class.java)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    override val notes: Flow<List<Note>> get() = _notes

    override fun addNote(note: Note) {
        _notes.value += note
        logger.debug("Добавлена заметка: ${note.title}")
        logger.debug("Новый размер массива заметок: ${_notes.value.size}")
    }

    override fun getNoteByUid(uid: String): Flow<Note> =
        flow {
            val habit = checkNotNull(_notes.value.find { it.uid == uid })
            emit(habit)
        }

    override fun updateNote(note: Note)  {
        val updatedNotes = _notes.value.toMutableList()
        val index = updatedNotes.indexOfFirst { it.uid == note.uid }
        if (index != -1) {
            updatedNotes[index] = note
            _notes.value = updatedNotes
        }
    }

    override fun deleteNote(uid: String) {
        _notes.value = _notes.value.filterNot { it.uid == uid }
        logger.debug("Удаление заметки UID=$uid, удалено")
    }

    override fun saveAllNotesToFile(context: Context) {
        val file = File(context.filesDir, "notes.json")
        val jsonArray = JSONArray()
        _notes.value.forEach { jsonArray.put(it.json) }
        file.writeText(jsonArray.toString())
        logger.debug("Сохранено ${_notes.value.size} заметок в файл")
    }

    override fun loadAllNotesFromFile(context: Context) {
        val file = File(context.filesDir, "notes.json")
        if (!file.exists()) {
            logger.debug("Файл не найден, загрузка пропущена")
            return
        }

        val jsonText = file.readText()
        val array = JSONArray(jsonText)
        val loadedNotes = mutableListOf<Note>()
        for (i in 0 until array.length()) {
            val jsonNote = array.getJSONObject(i)
            Note.parse(jsonNote)?.let { loadedNotes.add(it) }
        }
        _notes.value = loadedNotes
        logger.debug("Загружено ${loadedNotes.size} заметок из файла")
    }

    override fun saveNoteToCache(note: Note, context: Context) {
        val file = File(context.filesDir, "note_${note.uid}.json")
        file.writeText(note.json.toString())
        logger.debug("Сохранена отдельная заметка UID=${note.uid}")
    }

    override fun loadNoteFromCache(uid: String, context: Context): Note? {
        val file = File(context.filesDir, "note_${uid}.json")
        if (!file.exists()) return null
        val jsonNote = org.json.JSONObject(file.readText())
        return Note.parse(jsonNote)
    }

    override fun deleteNoteFromCache(uid: String, context: Context) {
        val file = File(context.filesDir, "note_${uid}.json")
        if (file.exists()) {
            file.delete()
            logger.debug("Удалена заметка из кэша UID=$uid")
        }
    }

    // ---------- Заглушки для сетевых операций ----------

    override suspend fun syncNoteToBackend(note: Note) {
        logger.info("Заглушка: отправка заметки на бэкенд: ${note.title}")
        // здесь будет вызов API в будущем
    }

    override suspend fun deleteNoteFromBackend(uid: String) {
        logger.info("Заглушка: удаление заметки с бэкенда UID=$uid")
    }

    override suspend fun fetchNotesFromBackend(): List<Note> {
        logger.info("Заглушка: загрузка заметок с бэкенда")
        return emptyList()
    }
}

