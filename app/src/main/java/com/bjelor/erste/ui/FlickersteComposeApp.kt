package com.bjelor.erste.ui

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.bjelor.erste.ui.imagedetail.ImageDetailScreen
import com.bjelor.erste.ui.imagegrid.ImageGridScreen
import com.bjelor.erste.ui.theme.FlickersteTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FlickersteComposeApp() {
    FlickersteTheme {
        val navController = rememberAnimatedNavController()

        AnimatedNavHost(
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
                ImageDetailScreen {
                    navController.popBackStack()
                }
            }
        }
    }
}