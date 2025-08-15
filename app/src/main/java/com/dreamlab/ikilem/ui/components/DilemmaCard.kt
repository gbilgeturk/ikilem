package com.dreamlab.ikilem.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DilemmaCard(
    optionA: String,
    optionB: String,
    onChooseA: () -> Unit,
    onChooseB: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressedA by remember { mutableStateOf(false) }
    var pressedB by remember { mutableStateOf(false) }
    val scaleA by animateFloatAsState(if (pressedA) 0.95f else 1f, label = "scaleA")
    val scaleB by animateFloatAsState(if (pressedB) 0.95f else 1f, label = "scaleB")

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Hangisini seçerdin?",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { onChooseA() },
                modifier = Modifier.fillMaxWidth().scale(scaleA),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )            ) {
                Text(optionA, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
            }
            Button(
                onClick = { onChooseB() },
                modifier = Modifier.fillMaxWidth().scale(scaleB),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )            ) {
                Text(optionB, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
