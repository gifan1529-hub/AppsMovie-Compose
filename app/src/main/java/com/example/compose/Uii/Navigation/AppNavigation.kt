package com.example.compose.Uii.Navigation

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.compose.AuthViewModel
import com.example.compose.Uii.Screen.BookingTicket.BookingTicketVM
import com.example.compose.Uii.Screen.BookingTicket.BuffrtItem.BuffetItemScreen
import com.example.compose.Uii.Screen.BookingTicket.Component.BookingTicket
import com.example.compose.Uii.Screen.BookingTicket.ResultPayment.ResultPaymentScreen
import com.example.compose.Uii.Screen.DetailFilm.DetailFilmScreen
import com.example.compose.Uii.Screen.EditUser.EditUserScreen
import com.example.compose.Uii.Screen.Favorite.FavoriteScreen
import com.example.compose.Uii.Screen.Home.Component.BottomBar
import com.example.compose.Uii.Screen.Home.HomeScreen
import com.example.compose.Uii.Screen.Login.LoginScreen
import com.example.compose.Uii.Screen.Search.SearchScreen
import com.example.compose.Uii.Screen.BookingTicket.SeatSelection.SeatSelectionScreen
import com.example.compose.Uii.Screen.SignIn.SignInScreen
import com.example.compose.Uii.Screen.SignUp.SignUpScreen
import com.example.compose.Uii.Screen.Ticket.TicketScreen
import com.example.compose.Uii.Screen.BookingTicket.TicketSelection.TicketSelectionScreen
import com.example.compose.Uii.Screen.DetailTicket.DetailTicketScreen
import com.example.compose.Uii.Screen.User.UserScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
){
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = modifier
                .fillMaxSize(),
//            enterTransition = {
//                slideIntoContainer(
//                    AnimatedContentTransitionScope.SlideDirection.Right,
//                    animationSpec = tween(500)
//                )
//            },
//            exitTransition = {
//                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500))
//            }
        ) {
            composable(route = "login") {
                LaunchedEffect(key1 = authViewModel.isLoggedIn()) {
                    if (authViewModel.isLoggedIn()) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                LoginScreen(navController = navController)
            }
            composable(route = "signup") {
                SignUpScreen(navController = navController)
            }
            composable (route = "ticketselection"){
                BookingTicket(navController = navController)
            }
            composable (route = "resultpayment"){
                ResultPaymentScreen(navController = navController)
            }
            composable (route = "buffet"){
                val bookingVM: BookingTicketVM = hiltViewModel(
                    LocalContext.current as ComponentActivity
                )
                BuffetItemScreen(navController = navController,
                    viewModel = bookingVM
                )
            }
            composable (
                route = "detailticket/{bookingId}",
                arguments = listOf(
                    navArgument("bookingId") { type = NavType.IntType }
                ),
                deepLinks = listOf(
                    navDeepLink { uriPattern = "app://movie/detailticket/{bookingId}" }
                )
            ){ backStackEntry ->
                val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: 0
                DetailTicketScreen(
                    navController = navController,
                    bookingId = bookingId
                )
            }
            composable(route = "signin") {
                SignInScreen(navController = navController)
            }
            composable(
                route = "home",
            ) {
                MainPagerScreen(navController = navController)
            }
            composable(
                route = "home?page={page}",
                arguments = listOf(navArgument("page") { defaultValue = 1 })
            ) { backStackEntry ->
                val page = backStackEntry.arguments?.getInt("page") ?: 1
                MainPagerScreen(navController = navController, startPage = page)
            }
//            composable(
//                route = "home",
////                enterTransition = {
////                    val fromRoute = initialState.destination.route
////                    when (fromRoute){
////                        "favorite" -> {
////                            fadeIn()
//////                            slideIntoContainer(
//////                                AnimatedContentTransitionScope.SlideDirection.Right,
//////                                tween(500)
//////                            )
////                        }
////                        "ticket" -> {
////                            fadeIn()
//////                            slideIntoContainer(
//////                                AnimatedContentTransitionScope.SlideDirection.Left,
//////                                tween(500)
//////                            )
////                        }
////                        else -> {
////                            fadeIn()
////                        }
////                    }
////                },
//
//            ) {
//                HomeScreen(navController = navController)
//            }
            composable(
                route = "user",
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    )
                }
            ) {
                UserScreen(navController = navController)
            }
            composable(
                route = "edituser/{userEmail}",
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("userEmail") ?: ""
                EditUserScreen(
                    navController = navController,
                    userEmail = email,
                    onEditSucces = { newEmail ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("refresh_trigger", true)
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = "search",
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    )
                }
            ) {
                SearchScreen(navController = navController,
                    onMovieClick = { movieId -> navController.navigate("detailfilm/$movieId") }
                )
            }
//            composable(
//                route = "favorite",
////                enterTransition = {
////                    slideIntoContainer(
////                        AnimatedContentTransitionScope.SlideDirection.Right,
////                        animationSpec = tween(500)
////                    )
////                },
//
//                ) {
//                FavoriteScreen(navController = navController)
//            }
//            composable(
//                route = "ticket",
////                enterTransition = {
////                    slideIntoContainer(
////                        AnimatedContentTransitionScope.SlideDirection.Left,
////                        animationSpec = tween(500)
////                    )
////                },
////                exitTransition = {
////                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500))
////                }
//            ) {
//                TicketScreen(navController = navController)
//            }
            composable(route = "detailfilm/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")
                DetailFilmScreen(
                    navController = navController,
                    movieId = movieId ?: ""
                )
            }
            composable(route = "seatselection") {
                SeatSelectionScreen(navController = navController)
            }
        }
    }

@Composable
fun MainPagerScreen (navController: NavController, startPage: Int = 1) {
    val screens = listOf("favorite", "home", "ticket")
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { screens.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(startPage) {
        pagerState.scrollToPage(startPage)
    }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        bottomBar = {
            BottomBar(
                selectedIndex = pagerState.currentPage,
                onHomeClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                onFavoriteClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                onTicketClick = { scope.launch { pagerState.animateScrollToPage(2) } }
            )
        }
    ) { _ ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize(),
            userScrollEnabled = true
        ) { page ->
            when (page) {
                0 -> FavoriteScreen(navController = navController)
                1 -> HomeScreen(navController = navController)
                2 -> TicketScreen(navController = navController)
            }
        }
    }
}





