package com.example.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SingleNoteResponse(
    val status: String,
    val element: TodoItemDto,
    val revision: Int
)
