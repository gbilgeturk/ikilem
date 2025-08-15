package com.dreamlab.ikilem.ui.debug

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirestoreDebugScreen(vm: FirestoreDebugViewModel = viewModel()) {
    val status by vm.status.collectAsState()
    val msg by vm.msg.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Firestore Debug") }) }) { pad ->
        Column(
            Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Durum: $status")
            Text("Mesaj: ${msg ?: "-"}")

            Button(onClick = { vm.writeTest() }, modifier = Modifier.fillMaxWidth()) {
                Text("Debug/hello → {\"msg\":\"Merhaba\"} yaz")
            }
            OutlinedButton(onClick = { vm.startListening() }, modifier = Modifier.fillMaxWidth()) {
                Text("Canlı dinlemeyi başlat")
            }
        }
    }
}