package com.example.data.local.mappers

import com.example.data.local.entity.NoteEntity
import com.example.model.Note
import java.time.LocalDateTime

fun NoteEntity.toDomain(): Note = Note(
    uid = this.uid,
    title = this.title,
    content = this.content,
    color = this.color,
    importance = this.importance,
    selfDestructDate = this.selfDestructDate
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    uid = this.uid,
    title = this.title,
    content = this.content,
    color = this.color,
    importance = this.importance,
    selfDestructDate = this.selfDestructDate
)
