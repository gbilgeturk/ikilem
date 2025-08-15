package com.dreamlab.ikilem.ui.screen.add

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDilemmaScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Yeni İkilem") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("v1'de sadece arayüz. Kaydetme v2'de DataStore/Room ile gelecek.")
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Seçenek A") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Seçenek B") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            Button(onClick = { /* TODO v2 */ }, modifier = Modifier.fillMaxWidth()) { Text("Ekle") }
        }
    }
}