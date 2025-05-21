package com.example.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SingleNoteRequest(val element: TodoItemDto)
