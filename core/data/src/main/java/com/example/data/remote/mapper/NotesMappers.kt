package com.example.data.remote.mapper

import android.graphics.Color
import com.example.data.remote.model.TodoItemDto
import com.example.model.Importance
import com.example.model.Note
import java.util.Date
import java.util.UUID

fun Note.toDto(deviceId: String): TodoItemDto = TodoItemDto(
    id = this.uid,
    text = this.title,
    importance = this.importance.engName,
    deadline = this.selfDestructDate,
    done = false,
    createdAt = this.createdAt ?: Date().time,
    changedAt = this.updatedAt ?: Date().time,
    lastUpdatedBy = deviceId,
    color = if (this.color != Color.WHITE) {
        String.format("#%06X", 0xFFFFFF and this.color)
    } else {
        null
    }
)

fun TodoItemDto.toModel(): Note = Note(
    uid = this.id,
    title = this.text,
    content = "",
    importance = when (this.importance.lowercase()) {
        "low" -> Importance.LOW
        "important" -> Importance.HIGH
        "normal" -> Importance.NORMAL
        else -> Importance.NORMAL
    },
    color = this.color?.let {
        try {
            Color.parseColor(it)
        } catch (e: IllegalArgumentException) {
            Color.WHITE
        }
    } ?: Color.WHITE,
    selfDestructDate = this.deadline,
    createdAt = this.createdAt,
    updatedAt = this.changedAt
)
