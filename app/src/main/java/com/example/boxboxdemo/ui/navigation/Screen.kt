package com.example.boxboxdemo.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details")
}