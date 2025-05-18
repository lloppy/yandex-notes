package com.example.yandexnotes.ui.screens.item.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.yandexnotes.R
import com.example.yandexnotes.ui.screens.item.NoteEntity
import java.util.Calendar

@Composable
fun NotesInputForm(
    noteEntity: NoteEntity,
    onValueChange: (NoteEntity) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showDateButton by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var showColorDialog by remember { mutableStateOf(false) }
    val currentColor = remember { mutableStateOf(Color.Red) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = noteEntity.title,
            label = { Text(text = stringResource(R.string.set_name)) },
            onValueChange = { onValueChange(noteEntity.copy(title = it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = noteEntity.content,
            label = { Text(text = stringResource(R.string.set_content)) },
            onValueChange = { onValueChange(noteEntity.copy(content = it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Default,
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp)
        )

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = noteEntity.selfDestructDate != null,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            showDateButton = true
                        } else {
                            onValueChange(noteEntity.copy(selfDestructDate = null))
                            showDateButton = false
                        }
                    }
                )
                Text(text = stringResource(R.string.enable_self_destruct))
            }
            if (showDateButton) {
                Button(onClick = {
                    showDatePicker = true
                }) {
                    Text(text = "Выбрать дату")
                }
                if (noteEntity.selfDestructDate != null) {
                    Text(text = "Дата самоуничтожения: ${noteEntity.selfDestructDate}")
                }
            }
        }

        ColorPicker(
            initialColor = Color(noteEntity.color),
            onColorSelected = {
                onValueChange(noteEntity.copy(color = it.value.toInt()))
            },
            onOpenFullPalette = {
                showColorDialog = true
            },
            customColor = Brush.horizontalGradient(
                colors = listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Green,
                    Color.Cyan,
                    Color.Blue,
                    Color.Magenta,
                    Color.Red
                )
            )
        )

        ImportanceSelector(
            noteEntity = noteEntity,
            onValueChange = onValueChange
        )

        if (showColorDialog) {
            ColorPaletteDialog(
                initialColor = currentColor.value,
                onDismiss = { showColorDialog = false },
                onColorSelected = {
                    currentColor.value = it
                    showColorDialog = false
                }
            )
        }

        if (showDatePicker) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val date = "$day.${month + 1}.$year"
                    onValueChange(noteEntity.copy(selfDestructDate = date))
                    showDatePicker = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}
