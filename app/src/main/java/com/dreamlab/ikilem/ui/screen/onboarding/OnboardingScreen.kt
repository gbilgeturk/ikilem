package com.dreamlab.ikilem.ui.screen.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dreamlab.ikilem.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { Prefs(context) }
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("İkilem'e hoş geldin!", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            Text("Kurallar basit: İki seçenekten birini seç, sonra diğerlerinin ne seçtiğini gör.")
        }
        Button(
            onClick = {
                scope.launch(Dispatchers.IO) { prefs.setOnboardingSeen(true) }
                onFinish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Başla")
        }
    }
}