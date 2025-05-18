package com.example.data.remote.model

data class GetNoteResponse(
    val status: String,
    val element: TodoItemDto,
    val revision: Int
)
