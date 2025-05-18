package com.example.model

import android.graphics.Color
import org.json.JSONObject
import java.time.LocalDateTime
import java.util.UUID
import android.graphics.Color as AndroidColor

data class Note(
    val uid: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val color: Int = AndroidColor.WHITE,
    val importance: Importance = Importance.NORMAL,
    val selfDestructDate: LocalDateTime? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
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
                    "NORMAL" -> Importance.NORMAL
                    else -> Importance.NORMAL
                }
                Note(uid, title, content, color, importance)
            } catch (e: Exception) {
                null
            }
        }
    }
}
