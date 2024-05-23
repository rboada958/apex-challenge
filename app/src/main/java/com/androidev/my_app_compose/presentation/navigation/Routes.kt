package com.androidev.my_app_compose.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidev.my_app_compose.presentation.screen.home.HomeScreen
import com.androidev.my_app_compose.presentation.screen.character.CharacterScreen
import com.androidev.my_app_compose.presentation.screen.splash.SplashScreen

const val splashNavigationRoute = "splash_route"
const val homeNavigationRoute = "home_route"
const val characterNavigationRoute = "character_route"

fun NavGraphBuilder.splashScreen(navController: NavController) {
    composable(route = splashNavigationRoute) {
        SplashScreen(navController = navController)
    }
}

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(route = homeNavigationRoute) {
        HomeScreen(navController = navController)
    }
}

fun NavGraphBuilder.characterScreen(navController: NavController) {
    composable(
        route = "${characterNavigationRoute}/{id}",
        arguments = listOf(navArgument("id") { type = NavType.StringType })
    ) {
        CharacterScreen(navController = navController)
    }
}
