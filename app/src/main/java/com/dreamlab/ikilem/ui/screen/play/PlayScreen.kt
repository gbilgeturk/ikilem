package com.dreamlab.ikilem.ui.screen.play
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dreamlab.ikilem.data.model.Category

@Composable
fun PlayScreen(categoryName: String? = null, vm: PlayViewModel = viewModel()) {
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

    Scaffold { pad ->
        Box(
            Modifier
                .padding(pad)
                .fillMaxSize()
        ) {
            Column(Modifier.fillMaxSize()) {
                // ÜST YARI (A - kırmızı)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(listOf(red.copy(alpha = 0.95f), red.copy(alpha = 0.75f)))
                        )
                        .clickable(enabled = !hasChosen) { vm.chooseA() }
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dilemma?.optionA ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }

                // ALT YARI (B - mavi)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(listOf(blue.copy(alpha = 0.95f), blue.copy(alpha = 0.75f)))
                        )
                        .clickable(enabled = !hasChosen) { vm.chooseB() }
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dilemma?.optionB ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Ortada "YA DA" rozeti
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("YA DA", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            // Seçim sonrası yüzde overlay
            if (hasChosen) {
                val total = (votes.first + votes.second).coerceAtLeast(1)
                val aPct = (votes.first * 100f / total).toInt()
                val bPct = (votes.second * 100f / total).toInt()

                // Üstte A yüzdesi
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = MaterialTheme.shapes.large,
                        shadowElevation = 6.dp
                    ) {
                        Text(
                            "A • %$aPct",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF111827),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Altta B yüzdesi
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = MaterialTheme.shapes.large,
                        shadowElevation = 6.dp
                    ) {
                        Text(
                            "B • %$bPct",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF111827),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Sonraki ikilem butonu
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 120.dp) // "YA DA" rozetinin altına
                ) {
                    Button(
                        onClick = { vm.next(category) },   // <<< ÖNEMLİ: load değil next
                        modifier = Modifier
                            .widthIn(min = 220.dp)
                    ) { Text("Sonraki ikilem") }
                }
            }
        }
    }
}