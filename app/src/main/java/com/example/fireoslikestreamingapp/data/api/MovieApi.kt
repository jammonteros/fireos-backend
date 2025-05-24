package com.example.fireoslikestreamingapp.data.api

import com.example.fireoslikestreamingapp.data.model.Movie
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {
    @GET("api/movies")
    suspend fun getMovies(): List<Movie>

    @GET("api/movies/{id}")
    suspend fun getMovieById(@Path("id") id: String): Movie
} 