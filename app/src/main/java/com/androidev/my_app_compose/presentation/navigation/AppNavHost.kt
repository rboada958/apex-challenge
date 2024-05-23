package com.androidev.my_app_compose.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


@Composable
fun AppNavHost(
    navHostController: NavHostController,
    startDestination: String = splashNavigationRoute,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        splashScreen(navController = navHostController)
        homeScreen(navController = navHostController)
        characterScreen(navController = navHostController)
    }
}