package com.example.compose.Uii.Screen.Search


import com.example.compose.ApiOffline.RoomApi
import com.example.compose.Uii.Screen.Home.MovieRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

sealed class SearchState{
    data class Success(val movies: List<RoomApi>) : SearchState()
    data class Error(val message: String) : SearchState()
    object Loading : SearchState()
    object EmptyQuery : SearchState()
}
class SearchUC @Inject constructor(
    private val repository: MovieRepository
){
    fun execute(query: String): Flow<SearchState> = flow {
        if (query.isBlank()) {
            emit(SearchState.EmptyQuery)
            return@flow
        }
        emit(SearchState.Loading)

        try {
            val results = repository.searchMovies(query)
            emit(SearchState.Success(results))
        } catch (e: Exception) {
            emit(SearchState.Error(e.message ?: "Unknown error"))
        }
    }

}