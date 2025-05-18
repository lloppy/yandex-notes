package com.example.data.remote.mapper

import com.example.data.remote.model.TodoItemDto
import com.example.model.Note

fun Note.toDto(deviceId: String): TodoItemDto = TodoItemDto(
    id = this.uid,
    text = this.title,
    importance = "basic",
    deadline = null,
    done = false,
    color = null,
    createdAt = this.createdAt!!, // WARNING!!!!
    changedAt = this.updatedAt!!,
    lastUpdatedBy = deviceId
)

fun TodoItemDto.toModel(): Note = Note(
    uid = this.id,
    title = this.text,
    content = "",
    createdAt = this.createdAt,
    updatedAt = this.changedAt
)
