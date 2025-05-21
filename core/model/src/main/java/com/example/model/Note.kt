package com.example.model

import android.graphics.Color
import org.json.JSONObject
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID
import android.graphics.Color as AndroidColor

data class Note(
    val uid: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val color: Int = AndroidColor.WHITE,
    val importance: Importance = Importance.NORMAL,
    val selfDestructDate: Long? = null, // Unix timestamp in seconds
    val createdAt: Long? = null,
    val updatedAt: Long? = Date().time,
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

val Note.json: JSONObject
    get() {
        val obj = JSONObject()
        obj.put("uid", uid)
        obj.put("title", title)
        obj.put("content", content)
        if (color != Color.WHITE) {
            obj.put("color", String.format("#%06X", 0xFFFFFF and color))
        }
        if (importance != Importance.NORMAL) {
            obj.put("importance", importance.name)
        }
        return obj
    }