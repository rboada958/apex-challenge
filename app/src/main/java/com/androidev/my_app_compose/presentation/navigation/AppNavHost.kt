package com.androidev.my_app_compose.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    navHostController: NavHostController,
    startDestination: String = splashNavigationRoute,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    SharedTransitionLayout {
        NavHost(
            navController = navHostController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            splashScreen(navController = navHostController)
            homeScreen(navController = navHostController, sharedTransitionScope = this@SharedTransitionLayout)
            characterScreen(navController = navHostController, sharedTransitionScope = this@SharedTransitionLayout)
        }
    }

}