package com.example.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PatchNotesRequest(val list: List<TodoItemDto>)
