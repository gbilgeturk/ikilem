package com.dreamlab.ikilem.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Ayarlar") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("Online istatistik ve hesap bağlantıları v2'de.")
        }
    }
}
