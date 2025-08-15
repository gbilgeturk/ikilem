package com.dreamlab.ikilem.ui.screen.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dreamlab.ikilem.data.seed.Seeder
import com.dreamlab.ikilem.ui.navigation.Route
import com.dreamlab.ikilem.util.isOnline

@Composable
fun SplashScreen(navController: NavController) {
    var checking by remember { mutableStateOf(true) }
    var online by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        checking = true
        online = isOnline(navController.context)
        checking = false
        if (online) {
            navController.navigate(Route.Home.route) {
                popUpTo(Route.Onboarding.route){inclusive = false}
                popUpTo(Route.Home.route) { inclusive = true }
            }
            Seeder.seedIfNeeded(context = context, force = false) { didWrite ->
                // didWrite true ise ilk kez yazıldı; false ise zaten vardı.
            }
        }

    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (checking) {
            CircularProgressIndicator()
        } else if (!online) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("İnternet bağlantısı yok. Devam edemezsin.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    checking = true
                    online = isOnline(navController.context)
                    checking = false
                    if (online) {
                        navController.navigate(Route.Home.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }) { Text("Tekrar dene") }
            }
        }
    }
}