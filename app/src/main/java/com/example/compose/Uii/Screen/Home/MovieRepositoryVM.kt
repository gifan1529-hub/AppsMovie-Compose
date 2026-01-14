package com.example.compose.Uii.Screen.Home

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.ApiOffline.RoomApi
import com.example.compose.ApiOffline.RoomDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieRepositoryVM @Inject constructor(
    private val getMovieUC: GetMovieUC,
    private val refreshMovieUC: RefreshMovieUC
) : ViewModel() {
    val allMovies: Flow<List<RoomApi>> = getMovieUC()

    init {
        refreshMovies()
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            refreshMovieUC()
        }
    }
}