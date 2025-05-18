package com.example.model.mapper

import android.graphics.Color
import com.example.model.Note
import org.json.JSONObject

val Note.json: JSONObject
    get() {
        val obj = JSONObject()
        obj.put("uid", uid)
        obj.put("title", title)
        obj.put("content", content)
        if (color != Color.WHITE) {
            obj.put("color", String.format("#%06X", 0xFFFFFF and color))
        }
        if (importance != com.example.model.Importance.NORMAL) {
            obj.put("importance", importance.name)
        }
        return obj
    }
