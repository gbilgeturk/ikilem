package com.dreamlab.ikilem.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ChoiceCard(
    text: String,
    onChoose: () -> Unit,
    gradient: Brush,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "scale")

    Card(
        modifier = modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (kotlin.math.abs(dragAmount) > 30f) {
                        pressed = true
                        onChoose()
                        pressed = false
                    }
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}