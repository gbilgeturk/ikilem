package com.dreamlab.ikilem.ui.screen.play

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dreamlab.ikilem.data.model.Category

@Composable
fun PlayScreen(categoryName: String? = null, vm: PlayViewModel = viewModel()) {
    // Route param -> enum
    val category = remember(categoryName) {
        Category.entries.firstOrNull { it.name == (categoryName ?: "") }
    }
    LaunchedEffect(category) { vm.load(category) }

    val dilemma by vm.current.collectAsState()
    val votes by vm.votes.collectAsState()
    val hasChosen by vm.hasChosen.collectAsState()

    // Renkler
    val red = Color(0xFFEF4444)
    val blue = Color(0xFF3B82F6)

    // --- Yüzdelere göre animasyonlu weight hesapları
    val total = (votes.first + votes.second).coerceAtLeast(1)
    val aFracRaw = votes.first.toFloat() / total.toFloat()
    val aTarget = if (hasChosen) aFracRaw else 0.5f
    // Her iki alanın da görünür kalması için min/max sınır
    val aClamped = aTarget.coerceIn(0.15f, 0.85f)
    val aWeight by animateFloatAsState(
        targetValue = aClamped,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "aWeight"
    )
    val bWeight = 1f - aWeight
    val optionAlpha = if (hasChosen) 0.55f else 1f

    Scaffold(    contentWindowInsets = WindowInsets(0) // 👈 hiçbir inset ekleme
    ) { pad ->
        Box(Modifier.padding(pad).fillMaxSize()) {

            // Arka plan: A/B dikey alanlar
            Column(Modifier.fillMaxSize()) {

                // ÜST YARI (A - kırmızı) -> weight animasyonlu
                Box(
                    modifier = Modifier
                        .weight(aWeight)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(red.copy(alpha = 0.95f), red.copy(alpha = 0.75f))
                            )
                        )
                        .clickable(enabled = !hasChosen) { vm.chooseA() }
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dilemma?.optionA.orEmpty(),
                        color = Color.White.copy(alpha = optionAlpha),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }

                // ALT YARI (B - mavi) -> weight animasyonlu
                Box(
                    modifier = Modifier
                        .weight(bWeight)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(blue.copy(alpha = 0.95f), blue.copy(alpha = 0.75f))
                            )
                        )
                        .clickable(enabled = !hasChosen) { vm.chooseB() }
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dilemma?.optionB.orEmpty(),
                        color = Color.White.copy(alpha = optionAlpha),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Ortadaki "YA DA" rozeti (seçim sonrası biraz saydam)
            val badgeAlpha by animateFloatAsState(
                targetValue = if (hasChosen) 0.6f else 1f,
                animationSpec = tween(300),
                label = "badgeAlpha"
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.92f * badgeAlpha))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "YA DA",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Seçim sonrası overlay + yüzdeler + "Sonraki İkilem"
            if (hasChosen) {
                val total = (votes.first + votes.second).coerceAtLeast(1)
                val aPct = (votes.first * 100f / total).toInt()
                val bPct = (votes.second * 100f / total).toInt()

                // Kazananı biraz daha belirgin yapalım
                val aWins = aPct >= bPct
                val chipScaleA = if (aWins) 1.0f else 0.98f
                val chipScaleB = if (aWins) 0.98f else 1.0f

                // A için: üst-sol köşe (status bar'dan uzak)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 24.dp, start = 16.dp)
                ) {
                    ResultChip(
                        text = "A • %$aPct",
                        scale = chipScaleA
                    )
                }

                // B için: alt-sağ köşe (nav bar'dan uzak)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 24.dp, end = 16.dp)
                ) {
                    ResultChip(
                        text = "B • %$bPct",
                        scale = chipScaleB
                    )
                }

                // Ortada “Sonraki İkilem” aynı kalsın…
                Box(Modifier.align(Alignment.Center)) {
                    Button(
                        onClick = { vm.next(category) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                            .widthIn(min = 220.dp)
                            .height(56.dp)
                    ) { Text("Sonraki İkilem", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
private fun ResultChip(text: String, scale: Float) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shape = MaterialTheme.shapes.large,
        shadowElevation = 8.dp,
        tonalElevation = 2.dp,
        modifier = Modifier
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
            }
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = Color(0xFF0F172A) // koyu gri, her zeminle okunur
        )
    }
}