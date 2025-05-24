package com.example.fireoslikestreamingapp.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fireoslikestreamingapp.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class User(
    val id: String,
    val username: String,
    val email: String
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val userPreferencesManager = UserPreferencesManager(application)

    init {
        // TODO: Implementar carga de usuario desde preferencias
    }

    fun signIn(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // TODO: Implementar autenticación real
                // Por ahora, simulamos una autenticación exitosa
                _currentUser.value = User(
                    id = "1",
                    username = "Usuario",
                    email = email
                )
                callback(true, null)
            } catch (e: Exception) {
                _error.value = e.message
                callback(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signUp(username: String, email: String, password: String, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // TODO: Implementar registro real
                // Por ahora, simulamos un registro exitoso
                _currentUser.value = User(
                    id = "1",
                    username = username,
                    email = email
                )
                callback(true, null)
            } catch (e: Exception) {
                _error.value = e.message
                callback(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _currentUser.value = null
            // TODO: Implementar limpieza de preferencias
        }
    }

    fun updateUserPreferences(preferences: UserPreferences) {
        viewModelScope.launch {
            try {
                userPreferencesManager.updateUsername(preferences.username)
                userPreferencesManager.updateEmail(preferences.email)
                userPreferencesManager.updateVideoQuality(preferences.videoQuality)
                userPreferencesManager.updateLanguage(preferences.language)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
} 