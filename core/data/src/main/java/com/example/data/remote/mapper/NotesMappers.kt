package com.example.data.remote.mapper

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.data.remote.model.TodoItemDto
import com.example.model.Importance
import com.example.model.Note
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Note.toDto(deviceId: String): TodoItemDto = TodoItemDto(
    id = this.uid,
    text = this.title,
    importance = this.importance.engName,
    selfDestructDate = this.selfDestructDate,
    done = false,
    createdAt = this.createdAt ?: System.currentTimeMillis(),
    changedAt = this.updatedAt ?: System.currentTimeMillis(),
    lastUpdatedBy = deviceId,
    color = if (this.color != Color.WHITE) {
        String.format("#%06X", 0xFFFFFF and this.color)
    } else {
        null
    },

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
    selfDestructDate = this.selfDestructDate,
    createdAt = this.createdAt,
    updatedAt = this.changedAt
)
