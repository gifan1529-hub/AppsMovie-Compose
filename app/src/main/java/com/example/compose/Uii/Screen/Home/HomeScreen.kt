package com.example.compose.Uii.Screen.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.ApiOffline.RoomApi
import com.example.compose.R
import com.example.compose.Uii.Screen.Home.Component.BottomBar
import com.example.compose.Uii.Screen.Home.Component.HorizontalMovieList
import com.example.compose.Uii.Screen.Home.Component.MovieCard
import com.example.compose.Uii.Screen.Home.Component.MovieCardSamping
import com.example.compose.Uii.Screen.Home.Component.TopBar
import com.example.compose.ui.theme.ComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MovieRepositoryVM = hiltViewModel()
){
    val movies by viewModel.allMovies.collectAsState(initial = emptyList())
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(R.drawable.bg2),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(R.drawable.bg3),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                item {
                    TopBar(
                        onProfileClick = {navController.navigate("user") },
                        onSearchClick = {navController.navigate("search") }
                    )
                }
                item {
                    HorizontalMovieList(
                        title = "Highlights"
                    )
                    val highlights = movies.take ( 5 )
                        LazyRow {
                            items(highlights) { movie ->
                                MovieCardSamping(
                                    movie = movie,
                                    onClick = {navController.navigate("detailfilm/${movie.id}") }
                                )
                            }
                        }

                }
                item {
                    HorizontalMovieList(
                        title = "Movie"
                    )
                    val highlights = movies.filter { it.type == "movie" }
                    LazyRow {
                        items(highlights) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = {navController.navigate("detailfilm/${movie.id}")  }
                            )
                        }
                    }

                }
                item {
                    HorizontalMovieList(
                        title = "Tv Series"
                    )
                    val highlights = movies.filter { it.type == "tvSeries" }
                    LazyRow {
                        items(highlights) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = {navController.navigate("detailfilm/${movie.id}")  }
                            )
                        }
                    }
                }
                item {
                    HorizontalMovieList(
                        title = "Tv Mini Series"
                    )
                    val highlights = movies.filter { it.type == "tvMiniSeries" }
                    LazyRow {
                        items(highlights) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = {navController.navigate("detailfilm/${movie.id}")  }
                            )
                        }
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier.height(110.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTheme {
        HomeScreen(navController = rememberNavController())
    }
}