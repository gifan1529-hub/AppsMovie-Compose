package com.example.compose.Uii.Screen.DetailFilm

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.ApiOffline.RoomApi
import com.example.compose.SharedPreferences
import com.example.compose.Uii.Screen.Favorite.FavoriteMovie
import com.example.compose.Uii.Screen.Favorite.MovieDao
import com.example.compose.Uii.Screen.Home.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFilmVM @Inject constructor(
    private val favoriteDao: MovieDao,
    private val sharedPrefs: SharedPreferences,
    private val repository: MovieRepository,
    private val getMovieDetailUC: GetMovieDetailUC
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailResult>(DetailResult.Loading)
    val uiState: StateFlow<DetailResult> = _uiState
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite


    fun getMovieById(movieId: String) {
        viewModelScope.launch {
            val localStatus = favoriteDao.isMovieFavorite(movieId) ?: false
            _isFavorite.value = localStatus

            getMovieDetailUC.execute(movieId).collect { result ->
                if (result is DetailResult.Success) {
                    result.movie.isFavorite = localStatus
                }
                _uiState.value = result
            }
        }
    }

    fun checkFavoriteStatus(movieId: String) {
        viewModelScope.launch {
            _isFavorite.value = favoriteDao.isMovieFavorite(movieId) ?: false
        }
    }

    fun toggleFavoriteStatus(movie: RoomApi) {
        viewModelScope.launch {
            val newStatus = !_isFavorite.value
            val userEmail = sharedPrefs.getUserEmail() ?: "guest@mail.com"

            movie.isFavorite = newStatus

            if (newStatus) {
                val favoriteMovie = FavoriteMovie(
                    id = movie.id,
                    email = userEmail,
                    title = movie.title,
                    posterUrl = movie.posterUrl,
                    plot = movie.plot,
                    rating = movie.rating
                )
                favoriteDao.insertMovie(favoriteMovie)
                favoriteDao.updateFavoriteStatus(movie.id, true)
            } else {
                favoriteDao.updateFavoriteStatus(movie.id, false)
            }

            _isFavorite.value = newStatus
        }
    }
}