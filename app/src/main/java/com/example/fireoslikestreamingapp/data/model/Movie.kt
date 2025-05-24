package com.example.fireoslikestreamingapp.data.model

data class Movie(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val rating: Double,
    val year: Int,
    val duration: String? = null,
    val director: String? = null,
    val cast: List<String>? = null,
    val videoUrl: String? = null
) 