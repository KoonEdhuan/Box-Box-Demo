package com.example.boxboxdemo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.boxboxdemo.ui.details.DetailsScreen
import com.example.boxboxdemo.ui.home.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDetails = {
                    navController.navigate(Screen.Details.route)
                }
            )
        }

        composable(Screen.Details.route) {
            DetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}