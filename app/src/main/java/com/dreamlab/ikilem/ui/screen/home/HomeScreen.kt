package com.dreamlab.ikilem.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Casino
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dreamlab.ikilem.data.model.Category
import com.dreamlab.ikilem.ui.navigation.Route

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeScreen(navController: NavController) {
    val data = categoryUiList() // 👈 statik liste

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { navController.navigate("play?cat=") }) {
                Text("Rastgele Oyna")
            }
        }
    ) { pad ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(pad)
                .padding(12.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(data) { it ->   // 👈 BURASI düzelti: items(items) değil
                ElevatedCard(
                    onClick = { navController.navigate("play?cat=${it.category.name}") },
                    shape = MaterialTheme.shapes.large
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Brush.linearGradient(listOf(it.colorStart, it.colorEnd)))
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(it.icon, contentDescription = null, tint = Color.White)
                            Column {
                                Text(it.title, color = Color.White, style = MaterialTheme.typography.titleMedium)
                                Text(it.subtitle, color = Color.White.copy(alpha = 0.85f))
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class CategoryUi(
    val category: Category,
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val colorStart: Color,
    val colorEnd: Color
)

private fun categoryUiList() = listOf(
    CategoryUi(Category.GUNLUK, "Günlük", "Günlük hayat ikilemleri", Icons.Outlined.Today, Color(0xFF6366F1), Color(0xFF22D3EE)),
    CategoryUi(Category.ASK_ILISKI, "Aşk & İlişki", "Kalp mi akıl mı?", Icons.Outlined.Favorite, Color(0xFFFF6B6B), Color(0xFFFFD93D)),
    CategoryUi(Category.KARİYER_PARA, "Kariyer & Para", "Tutku vs gelir", Icons.Outlined.Money, Color(0xFF10B981), Color(0xFF60A5FA)),
    CategoryUi(Category.MACERA, "Macera", "Konfor alanı dışı", Icons.Outlined.Explore, Color(0xFFFB7185), Color(0xFFF59E0B)),
    CategoryUi(Category.FANTASTIK, "Fantastik", "Gerçek üstü seçimler", Icons.Outlined.Psychology, Color(0xFF8B5CF6), Color(0xFFEC4899))
)