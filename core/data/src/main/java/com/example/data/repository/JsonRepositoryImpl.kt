package com.example.data.repository

import android.content.Context
import com.example.domain.JsonRepository
import com.example.model.Note
import com.example.model.mapper.json
import org.slf4j.LoggerFactory
import java.io.File

class JsonRepositoryImpl: JsonRepository {
    private val logger = LoggerFactory.getLogger(FileNotebook::class.java)

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
    
}