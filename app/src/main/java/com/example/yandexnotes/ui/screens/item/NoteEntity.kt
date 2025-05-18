package com.example.yandexnotes.ui.screens.item

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.model.Importance
import com.example.model.Note
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class NoteEntity(
    val uid: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val color: Int = android.graphics.Color.WHITE,
    val importance: Importance = Importance.NORMAL,
    val selfDestructDate: String? = null
)

@RequiresApi(Build.VERSION_CODES.O)
fun NoteEntity.toNote(): Note = Note(
    uid = uid,
    title = title,
    content = content,
    color = color,
    importance = importance,
    selfDestructDate = selfDestructDate?.let {
        try {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (e: Exception) {
            null
        }
    }
)


@RequiresApi(Build.VERSION_CODES.O)
fun Note.toUiState(): NoteEntity = NoteEntity(
    uid = uid,
    title = title,
    content = content,
    color = color,
    importance = importance,
    selfDestructDate = selfDestructDate?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
)
