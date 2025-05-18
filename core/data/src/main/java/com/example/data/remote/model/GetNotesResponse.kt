package com.example.data.remote.model

data class GetNotesResponse(
    val status: String,
    val list: List<TodoItemDto>,
    val revision: Int
)