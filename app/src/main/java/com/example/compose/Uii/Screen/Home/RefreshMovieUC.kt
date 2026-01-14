package com.example.compose.Uii.Screen.Home

import com.example.compose.Uii.Screen.Home.MovieRepository
import javax.inject.Inject

class RefreshMovieUC @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() {
        repository.refreshMovies()
    }
}