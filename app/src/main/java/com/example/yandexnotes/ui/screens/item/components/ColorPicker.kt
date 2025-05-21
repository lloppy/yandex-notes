package com.example.yandexnotes.ui.screens.item.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.yandexnotes.R

@Composable
fun ColorPicker(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onOpenFullPalette: () -> Unit,
) {
    var selectedColor by remember { mutableStateOf(initialColor) }

    val defaultColors = listOf(
        Color.Red,
        Color(0xFFFF9800),
        Color(0xFF4CAF50),
        Color(0xFF03A9F4),
        Color.Yellow
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.select_color))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            defaultColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color)
                        .border(
                            width = if (selectedColor == color) 2.dp else 0.dp,
                            color = if (selectedColor == color) Color.Black else Color.Transparent
                        )
                        .pointerInput(Unit) {
                            detectTapGestures {
                                selectedColor = color
                                onColorSelected(color)
                            }
                        }
                ) {
                    if (selectedColor == color) {
                        Text(
                            text = "✓",
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .align(Alignment.TopStart)
                        )
                    }
                }
            }

            RainbowBox(
                defaultColors = defaultColors,
                selectedColor = selectedColor,
                onOpenFullPalette = onOpenFullPalette
            )
        }
    }
}

@Composable
private fun RainbowBox(
    defaultColors: List<Color>,
    selectedColor: Color,
    onOpenFullPalette: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                brush = Brush.horizontalGradient(
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
            .border(
                width = if (!defaultColors.contains(selectedColor)) 2.dp else 0.dp,
                color = if (!defaultColors.contains(selectedColor)) Color.Black else Color.Transparent
            )
            .pointerInput(Unit) {
                detectTapGestures {
                    onOpenFullPalette()
                }
            }
    ) {
        if (!defaultColors.contains(selectedColor)) {
            Text(
                text = "✓",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .align(Alignment.TopCenter)
            )
        }
    }
}
