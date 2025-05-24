package com.example.fireoslikestreamingapp.data

data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val avatarUrl: String = "",
    val preferences: UserPreferences = UserPreferences()
)

data class UserPreferences(
    val autoPlay: Boolean = true,
    val quality: String = "auto",
    val language: String = "es",
    val notifications: Boolean = true
) 