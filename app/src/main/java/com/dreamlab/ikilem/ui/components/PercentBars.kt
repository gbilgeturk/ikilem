package com.dreamlab.ikilem.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.max
@Composable
fun PercentBars(aVotes: Int, bVotes: Int, modifier: Modifier = Modifier) {
    val total = max(aVotes + bVotes, 1)
    val aPct = aVotes.toFloat() / total.toFloat()
    val bPct = bVotes.toFloat() / total.toFloat()

    val aAnim by animateFloatAsState(targetValue = aPct, label = "aAnim")
    val bAnim by animateFloatAsState(targetValue = bPct, label = "bAnim")

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Sonuçlar", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("A • %${(aAnim * 100).toInt()}")
            LinearProgressIndicator(progress = aAnim, modifier = Modifier.fillMaxWidth())
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("B • %${(bAnim * 100).toInt()}")
            LinearProgressIndicator(progress = bAnim, modifier = Modifier.fillMaxWidth())
        }
        Text("$total oy", modifier = Modifier.padding(top = 4.dp))
    }
}