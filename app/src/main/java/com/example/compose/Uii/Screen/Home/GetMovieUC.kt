package com.example.compose.Uii.Screen.Home

import com.example.compose.ApiOffline.RoomApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieUC @Inject constructor (
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<RoomApi>> {
        return repository.getMoviesFromLocal()
    }
}