package com.bjelor.erste.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bjelor.erste.ui.imagedetail.ImageDetailScreen
import com.bjelor.erste.ui.imagegrid.ImageGridScreen
import com.bjelor.erste.ui.theme.FlickersteTheme

@Composable
fun FlickersteComposeApp(
    onShare: (String) -> Unit,
) {
    FlickersteTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = NavRoute.ImageGrid
        ) {
            composable(
                route = NavRoute.ImageGrid,
                enterTransition = {
                    slideInHorizontally { fullWidth -> fullWidth }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        NavRoute.ImageDetail -> {
                            fadeOut()
                        }
                        else -> {
                            ExitTransition.None
                        }
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        NavRoute.ImageDetail -> {
                            fadeIn()
                        }
                        else -> {
                            EnterTransition.None
                        }
                    }
                }
            ) {
                ImageGridScreen { url ->
                    navController.navigate(NavRoute.ImageDetail + "/$url")
                }
            }
            composable(
                route = NavRoute.ImageDetail + "/{url}",
                arguments = listOf(
                    navArgument("url") {
                        type = NavType.StringType
                    }
                ),
                enterTransition = {
                    fadeIn()
                },
                exitTransition = {
                    fadeOut()
                },
            ) {
                ImageDetailScreen(onShare) {
                    navController.popBackStack()
                }
            }
        }
    }
}