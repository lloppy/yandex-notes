package com.example.domain

import android.content.Context
import com.example.model.Note

interface JsonRepository {

    // cache
    fun saveNoteToCache(note: Note, context: Context)
    fun loadNoteFromCache(uid: String, context: Context): Note?
    fun deleteNoteFromCache(uid: String, context: Context)

}