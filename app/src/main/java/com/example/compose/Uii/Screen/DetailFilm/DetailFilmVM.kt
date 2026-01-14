package com.example.compose.Uii.Screen.DetailFilm

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFilmVM @Inject constructor(
    private val getMovieDetailUC: GetMovieDetailUC
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailResult>(DetailResult.Loading)
    val uiState: StateFlow<DetailResult> = _uiState

    fun getMovieById(movieId: String) {
        viewModelScope.launch {
            getMovieDetailUC.execute(movieId).collect { result ->
                _uiState.value = result
            }
        }
    }
}