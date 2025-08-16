package com.dreamlab.ikilem.ui.screen.splash

import android.app.Activity
import androidx.activity.ComponentActivity
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
import com.dreamlab.ikilem.util.Prefs
import com.dreamlab.ikilem.util.isOnline
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.net.HttpURLConnection
import java.net.URL
import kotlin.system.exitProcess

@Composable
fun SplashScreen(navController: NavController) {
    val activity = LocalContext.current as? ComponentActivity
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
    val prefs = remember { Prefs(context) }

    LaunchedEffect(Unit) {
        val hasInternet = checkInternetConnection()
        delay(1500) // kısa bir splash animasyon süresi
        if (!hasInternet) {
            online = false
        } else {
            online = true
            if (prefs.onboardingSeen.first()) {
                navController.navigate(Route.Home.route) {
                    popUpTo(Route.Splash.route) { inclusive = true }
                }
            } else {
                navController.navigate(Route.Onboarding.route) {
                    popUpTo(Route.Splash.route) { inclusive = true }
                }
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
                Button(onClick = {
                    activity?.finishAffinity()
                    exitProcess(0)
                }) { Text("Kapat") }
            }
        }
    }
}

fun checkInternetConnection(): Boolean {
    return try {
        val url = URL("https://www.google.com")
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 2000
        conn.connect()
        conn.responseCode == 200
    } catch (e: Exception) {
        false
    }
}