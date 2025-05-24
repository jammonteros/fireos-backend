package com.example.fireoslikestreamingapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Search : Screen("search")
    object Library : Screen("library")
    object Profile : Screen("profile")
    object Player : Screen("player/{videoUrl}/{title}") {
        fun createRoute(videoUrl: String, title: String) = "player/$videoUrl/$title"
    }
} 