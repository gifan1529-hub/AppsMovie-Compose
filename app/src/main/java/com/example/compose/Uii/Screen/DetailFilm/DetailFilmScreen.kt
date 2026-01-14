package com.example.compose.Uii.Screen.DetailFilm

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.compose.R
import com.example.compose.Uii.Screen.BookingTicket.BookingTicketVM
import com.example.compose.Uii.Screen.Favorite.FavoriteScreen
import com.example.compose.ui.theme.ComposeTheme
import com.example.compose.ui.theme.Poppins

@Composable
fun DetailFilmScreen(
    navController: NavController,
    movieId: String,
    viewModel: DetailFilmVM = hiltViewModel(),
    bookingVM: BookingTicketVM = hiltViewModel(LocalContext.current as ComponentActivity)
) {

    LaunchedEffect(movieId) {
        viewModel.getMovieById(movieId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        val movie = (uiState as? DetailResult.Success)?.movie
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    AsyncImage(
                        model = movie?.posterUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xFF1A1A1A)),
                                    startY = 100f
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { navController.navigate("home") },
                            modifier = Modifier.background(Color.Black.copy(0.4f), CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.bintang),
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    AsyncImage(
                        model = movie?.posterUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .size(width = 140.dp, height = 210.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .align(Alignment.BottomStart),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 180.dp, bottom = 10.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            text = movie?.title ?: "Loading...",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = movie?.releaseYear?.toString() ?: "Loading...",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = Poppins
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val ratingValue = movie?.rating ?: 0.0
                            val filledStars = (ratingValue / 2).toInt()
                            repeat(5) { index ->
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < filledStars) Color.Yellow else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "$ratingValue/10",
                                color = Color.Gray,
                                fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Genre",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        fontFamily = Poppins
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = movie?.genre ?: "Loading...",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Poppins)
                }
            }
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(text = "Plot",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        fontFamily = Poppins)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie?.plot ?: "Loading...",
                        color = Color.LightGray,
                        lineHeight = 22.sp,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Poppins
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
        Button(
            onClick = {
                if (movie != null) {
                    bookingVM.setInitialMovieData(
                        id = movie.id,
                        title = movie.title,
                        posterUrl = movie.posterUrl,
                        genre = movie.genre
                    )
                    Log.d("DetailFilmScreen", "data ${movie.id}${movie.title}${movie.posterUrl}${movie.genre}")
                    navController.navigate("ticketselection")
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(24.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C1A78)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Buy Ticket Now",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Poppins,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTheme {

    }
}