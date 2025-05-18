package com.example.yandexnotes.ui.screens.item

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.example.yandexnotes.R

@Composable
fun NotesInputForm(
    noteEntity: NoteEntity,
    onValueChange: (NoteEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
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
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Sentences
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}