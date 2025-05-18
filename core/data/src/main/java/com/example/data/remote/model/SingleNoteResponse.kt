package com.example.data.remote.model

data class SingleNoteResponse(
    val status: String,
    val element: TodoItemDto,
    val revision: Int
)
