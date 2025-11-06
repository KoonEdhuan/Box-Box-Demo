package com.example.boxboxdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.boxboxdemo.ui.navigation.AppNavHost
import com.example.boxboxdemo.ui.theme.BoxBoxDemoTheme
import com.example.boxboxdemo.ui.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: MyViewModel = hiltViewModel()
            BoxBoxDemoTheme {
                AppNavHost(navController = navController)
            }
        }
    }
}
