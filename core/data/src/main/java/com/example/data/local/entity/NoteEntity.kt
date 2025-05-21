package com.example.data.local.entity

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.data.local.mappers.Converters
import com.example.model.Importance
import java.time.LocalDateTime

@Entity(tableName = "notes")
@TypeConverters(Converters::class)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uid: String = "",
    val title: String = "",
    val content: String = "",
    val color: Int = Color.WHITE,
    val importance: Importance = Importance.NORMAL,

    @ColumnInfo("self_destruct_date")
    val selfDestructDate: Long? = null,
)
