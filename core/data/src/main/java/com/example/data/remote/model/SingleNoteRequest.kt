package com.example.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleNoteRequest(
    @SerialName("element") val element: TodoItemDto
)