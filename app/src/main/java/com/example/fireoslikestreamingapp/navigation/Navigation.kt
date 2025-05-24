package com.example.fireoslikestreamingapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fireoslikestreamingapp.screens.*
import com.example.fireoslikestreamingapp.viewmodel.AuthViewModel
import com.example.fireoslikestreamingapp.viewmodel.MoviesViewModel

@Composable
fun Navigation(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val moviesViewModel: MoviesViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }}
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onRegisterSuccess = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Register.route) { inclusive = true }
                }}
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToPlayer = { videoUrl, title ->
                    try {
                        navController.navigate(
                            "player/" + java.net.URLEncoder.encode(videoUrl, "UTF-8") + "/" + java.net.URLEncoder.encode(title, "UTF-8")
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen()
        }

        composable(Screen.Library.route) {
            LibraryScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "player/{videoUrl}/{title}",
        ) { backStackEntry ->
            val videoUrl = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("videoUrl") ?: "", "UTF-8")
            val title = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", "UTF-8")
            PlayerScreen(
                videoUrl = videoUrl,
                title = title,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
} 