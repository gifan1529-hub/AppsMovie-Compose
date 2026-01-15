package com.example.compose.Uii.Screen.Favorite


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.ApiOffline.RoomApi
import com.example.compose.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class FavoriteVM @Inject constructor(
    private val favoriteDao: MovieDao,
    private val sharedPrefs: SharedPreferences
) : ViewModel() {
    private val userEmail = sharedPrefs.getUserEmail() ?: ""

    val favoriteMovies: StateFlow<List<FavoriteMovie>> = favoriteDao.getAllFavoriteMovies(userEmail)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFromFavorite(movie: RoomApi) {
        viewModelScope.launch {
            val updatedMovie = movie.copy(isFavorite = false)
            favoriteDao.updateFavoriteStatus(updatedMovie.id, false)
        }
    }
}