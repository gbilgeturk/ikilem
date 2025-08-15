package com.dreamlab.ikilem.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dreamlab.ikilem.ui.debug.FirestoreDebugScreen
import com.dreamlab.ikilem.ui.screen.add.AddDilemmaScreen
import com.dreamlab.ikilem.ui.screen.home.HomeScreen
import com.dreamlab.ikilem.ui.screen.onboarding.OnboardingScreen
import com.dreamlab.ikilem.ui.screen.play.PlayScreen
import com.dreamlab.ikilem.ui.screen.settings.SettingsScreen


sealed class Route(val route: String) {
    data object Splash : Route("splash")
    data object Onboarding : Route("onboarding")
    data object Home : Route("home")
    data object Play : Route("play?cat={cat}") // 👈 opsiyonel arg
    data object Add : Route("add")
    data object Settings : Route("settings")
}

@Composable
fun AppNavHost(navController: NavHostController, startDest: String) {
    NavHost(navController = navController, startDestination = startDest) {
        composable(Route.Splash.route) { com.dreamlab.ikilem.ui.screen.splash.SplashScreen(navController) }
        composable(Route.Onboarding.route) {
            OnboardingScreen(onFinish = {
                navController.navigate("home") { popUpTo("onboarding") { inclusive = true } }
            })
        }
        composable(Route.Home.route) { HomeScreen(navController) }
        composable(
            route = "play?cat={cat}",
            arguments = listOf(navArgument("cat") { type = NavType.StringType; defaultValue = "" })
        ) { backStack ->
            val catName = backStack.arguments?.getString("cat")
            PlayScreen(categoryName = catName)
        }
        composable(Route.Add.route) { AddDilemmaScreen() }
        composable(Route.Settings.route) { SettingsScreen() }
    }
}