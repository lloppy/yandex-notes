package com.example.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GetNotesResponse(
    val status: String,
    val list: List<TodoItemDto>,
    val revision: Int
)