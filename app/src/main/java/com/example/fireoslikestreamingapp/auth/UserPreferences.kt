package com.example.fireoslikestreamingapp.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

data class UserPreferences(
    val username: String = "",
    val email: String = "",
    val videoQuality: String = "Auto",
    val language: String = "Español",
    val notificationsEnabled: Boolean = true
)

class UserPreferencesManager(private val context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val VIDEO_QUALITY = stringPreferencesKey("video_quality")
        private val LANGUAGE = stringPreferencesKey("language")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            username = preferences[USERNAME] ?: "",
            email = preferences[EMAIL] ?: "",
            videoQuality = preferences[VIDEO_QUALITY] ?: "Auto",
            language = preferences[LANGUAGE] ?: "Español"
        )
    }

    suspend fun updateUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
    }

    suspend fun updateEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    suspend fun updateVideoQuality(quality: String) {
        dataStore.edit { preferences ->
            preferences[VIDEO_QUALITY] = quality
        }
    }

    suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }
} 