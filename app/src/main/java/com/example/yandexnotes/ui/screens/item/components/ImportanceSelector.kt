package com.example.yandexnotes.ui.screens.item.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.Importance.entries
import com.example.yandexnotes.ui.screens.item.NoteEntity

@Composable
fun ImportanceSelector(
    noteEntity: NoteEntity,
    onValueChange: (NoteEntity) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(entries) { level ->
            FilterChip(
                selected = noteEntity.importance == level,
                onClick = {
                    onValueChange(
                        noteEntity.copy(importance = level)
                    )
                },
                label = { Text(text = level.rusName) }
            )
        }
    }
}
