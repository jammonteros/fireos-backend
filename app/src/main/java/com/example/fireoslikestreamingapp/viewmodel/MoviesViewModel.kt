package com.example.fireoslikestreamingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fireoslikestreamingapp.data.api.RetrofitClient
import com.example.fireoslikestreamingapp.data.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = RetrofitClient.movieApi.getMovies()
                _movies.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar las películas"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchMovieById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = RetrofitClient.movieApi.getMovieById(id)
                _selectedMovie.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar la película"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 