package com.example.yandexnotes.ui.screens.item

import com.example.yandexnotes.model.Importance
import com.example.yandexnotes.model.Note
import java.util.UUID

data class NoteEntity(
    val uid: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val color: Int = android.graphics.Color.WHITE,
    val importance: Importance = Importance.NORMAL,
)

fun NoteEntity.toNote(): Note = Note(
    uid = uid,
    title = title,
    content = content,
    color = color,
    importance = importance
)


fun Note.toUiState(): NoteEntity = NoteEntity(
    uid = uid,
    title = title,
    content = content,
    color = color,
    importance = importance
)
