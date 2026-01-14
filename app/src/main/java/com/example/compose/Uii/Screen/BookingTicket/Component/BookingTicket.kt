package com.example.compose.Uii.Screen.BookingTicket.Component

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.R // Pastikan import R kamu benar
import com.example.compose.SharedPreferences
import com.example.compose.Uii.Screen.BookingTicket.BookingTicketVM
import com.example.compose.Uii.Screen.BookingTicket.Payment.PaymentScreen
import com.example.compose.Uii.Screen.BookingTicket.SeatSelection.SeatSelectionScreen
import com.example.compose.Uii.Screen.BookingTicket.TicketSelection.TicketSelectionScreen
import com.example.compose.ui.theme.ComposeTheme
import com.example.compose.ui.theme.Poppins // Pastikan font family kamu benar

@Composable
fun BookingTicket(navController: NavController) {
    var currentStep by remember { mutableStateOf(1) }
    val bookingVM: BookingTicketVM = hiltViewModel(LocalContext.current as ComponentActivity)
    var selectedPaymentMethod by remember { mutableStateOf("Bank Transfer") }
    val bookingData by bookingVM.bookingData.observeAsState()
    val context = LocalContext.current
    val sharedPrefs = remember { SharedPreferences(context) }
    val userEmail = sharedPrefs.getUserEmail() ?: " "


    Scaffold(
        containerColor = Color.Black,
        topBar = {
            BookingHeader(
                currentStep = currentStep,
                onBackClick = {
                    if (currentStep > 1) currentStep-- else navController.popBackStack()
                }
            )
        },
        bottomBar = {
            BookingFooter(
                currentStep = currentStep,
                onNextClick = {
                    when (currentStep) {
                        1 -> {
                            val isTheaterSelected = !bookingData?.theater.isNullOrEmpty()
                            val isSessionSelected = !bookingData?.session.isNullOrEmpty()

                            if (isTheaterSelected && isSessionSelected) {
                                currentStep++
                            } else {
                                Toast.makeText(
                                    context,
                                    "Harap pilih Theater dan Sesi",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        2 -> {
                            val totalTickets = (bookingData?.adultTickets ?: 0) + (bookingData?.childTickets ?: 0)
                            val selectedSeatsCount = bookingData?.selectedSeats?.size ?: 0

                            if (totalTickets > 0 && selectedSeatsCount == totalTickets) {
                                currentStep++
                            } else if (totalTickets == 0) {
                                Toast.makeText(context, "Harap tambahkan jumlah tiket", android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Harap pilih $totalTickets kursi", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        3 -> {
                            bookingVM.confirmPaymentAndSave(
                                method = selectedPaymentMethod,
                                userEmail = userEmail
                            )
                            navController.navigate("resultpayment") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
//                    if (currentStep < 3) {
//                        currentStep++
//                    } else {
//                        bookingVM.confirmPaymentAndSave(
//                            method = selectedPaymentMethod,
//                            userEmail = userEmail
//                        )
//                        navController.navigate("resultpayment") {
//                            popUpTo("home") { inclusive = true }
//                        }
//                    }
                }
            )
        }
    ) { _ ->
        val pagerState = rememberPagerState(pageCount = { 3 })
        LaunchedEffect(currentStep) {
            pagerState.animateScrollToPage(currentStep - 1)
        }
        LaunchedEffect(pagerState.currentPage) {
            currentStep = pagerState.currentPage + 1
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize(),
            userScrollEnabled = false // biar bisa scroll kanan kiri manula
        ) { page ->
            when (page) {
                0 -> TicketSelectionScreen(
                    navController = navController,
                    viewModel = bookingVM
                )
                1 -> SeatSelectionScreen(navController = navController)
                2 -> PaymentScreen(
                    navController = navController,
                    viewModel = bookingVM,
                    onOptionSelected = { selectedPaymentMethod = it }
                )
            }
        }
    }
}

@Composable
fun BookingHeader(currentStep: Int, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(40.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .alpha(0.5f)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f))
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..3) {
                StepCircle(
                    stepNumber = i,
                    isActive = i == currentStep
                )
                if (i < 3) Spacer(modifier = Modifier.width(8.dp))
            }
        }
       Spacer(modifier = Modifier.size(35.dp))
    }
}

@Composable
fun StepCircle(stepNumber: Int, isActive: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (isActive) Color(0xFF3C1A78) else Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stepNumber.toString(),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Poppins
        )
    }
}

@Composable
fun BookingFooter(currentStep: Int, onNextClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .navigationBarsPadding() // biar ga tertutup tombol navigasi
    ) {
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C1A78)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (currentStep < 3) "Next" else "Confirm Payment",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTheme {
        BookingTicket(navController = rememberNavController())
    }
}