package com.example.data.repository

import android.content.Context
import com.example.domain.NotesRepository
import com.example.domain.RemoteDataSource
import com.example.model.Note
import com.example.model.util.Result
import com.example.model.mapper.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.slf4j.LoggerFactory
import java.io.File

class FileNotebook(
    private val remoteDataSource: RemoteDataSource
) : NotesRepository {

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

    override fun updateNote(note: Note) {
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


    // ---------- Реальные сетевые операции вместо заглушек ----------

    override suspend fun syncNoteToBackend(note: Note, deviceId: String) {
        when (val result = remoteDataSource.updateNote(
            note = note,
            deviceId = deviceId
        )) {
            is Result.Success -> {

                val updatedNotes = _notes.value.toMutableList()
                val index = updatedNotes.indexOfFirst { it.uid == note.uid }
                if (index != -1) {
                    updatedNotes[index] = note
                    _notes.value = updatedNotes
                }
                logger.info("Заметка успешно синхронизирована: ${note.title}")
            }
            is Result.Error -> {
                logger.error("Ошибка синхронизации заметки: ${result.error}")
                throw Exception("Network error: ${result.error}")
            }
        }
    }

    override suspend fun saveNoteToBackend(note: Note, deviceId: String) {
        when (val result = remoteDataSource.addNote(
            note = note,
            deviceId = deviceId
        )) {
            is Result.Success -> {
                _notes.value += note
                logger.info("Заметка успешно синхронизирована: ${note.title}")
            }
            is Result.Error -> {
                logger.error("Ошибка синхронизации заметки: ${result.error}")
                throw Exception("Network error: ${result.error}")
            }
        }
    }

    override suspend fun deleteNoteFromBackend(uid: String) {
        when (remoteDataSource.deleteNote(uid)) {
            is Result.Success -> {
                logger.info("Заметка успешно удалена с сервера UID=$uid")
            }
            is Result.Error -> {
                logger.error("Ошибка удаления заметки: $uid")
                throw Exception("Network error")
            }
        }
    }

    override suspend fun fetchNotesFromBackend(): List<Note> {
        return when (val result = remoteDataSource.getNotes()) {
            is Result.Success -> {
                logger.info("Успешно загружено ${result.data.size} заметок с сервера")
                result.data.also { notes ->
                    _notes.value = notes
                }
            }
            is Result.Error -> {
                logger.error("Ошибка загрузки заметок с сервера")
                throw Exception("Сетевая ошибка: ${result.error}")
            }
        }
    }
}

