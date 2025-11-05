package com.example.boxboxdemo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.boxboxdemo.ui.details.DetailsScreen
import com.example.boxboxdemo.ui.home.HomeScreen
import com.example.boxboxdemo.ui.viewmodel.MyViewModel

@Composable
fun NavHost(
    navController: NavHostController,
    viewModel: MyViewModel
) {

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("details_screen") {
            DetailsScreen(
                navController = navController
            )
        }
    }
}