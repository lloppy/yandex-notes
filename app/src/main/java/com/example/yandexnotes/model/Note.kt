package com.example.yandexnotes.model

import android.graphics.Color
import org.json.JSONObject
import java.util.UUID
import android.graphics.Color as AndroidColor

data class Note(
    val uid: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val color: Int = AndroidColor.WHITE,
    val importance: Importance = Importance.NORMAL,
){
    companion object{
        fun parse(json: JSONObject): Note? {
            return try {
                val uid = json.optString("uid", UUID.randomUUID().toString())
                val title = json.getString("title")
                val content = json.getString("content")
                val color = if (json.has("color")) Color.parseColor(json.getString("color")) else Color.WHITE
                val importance = when (json.optString("importance", "NORMAL")) {
                    "LOW" -> Importance.LOW
                    "HIGH" -> Importance.HIGH
                    else -> Importance.NORMAL
                }
                Note(uid, title, content, color, importance)
            } catch (e: Exception) {
                null
            }
        }
    }
}
