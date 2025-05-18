package com.example.data.local.mappers

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.model.Importance
import com.example.model.NoteCategory
import com.example.model.NotePriority
import com.example.model.NoteType

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun noteImportanceToString(importance: Importance): String {
        return importance.name
    }

    @TypeConverter
    fun stringToNoteImportance(importanceName: String): Importance {
        return try {
            Importance.valueOf(importanceName)
        } catch (e: IllegalArgumentException) {
            Importance.NORMAL
        }
    }
}