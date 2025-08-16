package com.dreamlab.ikilem

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.dreamlab.ikilem.ui.navigation.AppNavHost
import com.dreamlab.ikilem.ui.navigation.Route
import com.dreamlab.ikilem.ui.theme.IkilemTheme
import com.dreamlab.ikilem.util.Prefs
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = com.google.firebase.ktx.Firebase.auth
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnSuccessListener { /* ready */ }
                .addOnFailureListener { it.printStackTrace() }
        }
        val prefs = Prefs(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                view.setPadding(
                    0,
                    insets.systemGestureInsets.top,
                    0,
                    insets.systemGestureInsets.bottom
                )
                insets
            }
            val insetsController =
                WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView())
            insetsController.setAppearanceLightStatusBars(true)
            insetsController.setAppearanceLightNavigationBars(true)
        }
        lifecycleScope.launch {
            val seen = prefs.onboardingSeen.first()
            setContent {
                IkilemTheme {
                    val nav = rememberNavController()
                    AppNavHost(navController = nav, startDest = Route.Splash.route)
                }
            }
        }
    }
}