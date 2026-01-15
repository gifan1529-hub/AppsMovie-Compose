package com.example.compose.Uii.Screen.User

import android.R.attr.icon
import android.R.attr.text
import android.R.id.icon
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.setValue
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.compose.R
import com.example.compose.SharedPreferences
import com.example.compose.ui.theme.ComposeTheme
import com.example.compose.ui.theme.Poppins

@Composable
fun UserScreen(
    navController : NavController,
    viewModel: UserVM = hiltViewModel()
) {

    val userDetails by viewModel.userDetails.observeAsState()

    val navBackStackEntry = navController.currentBackStackEntry
    val refreshTrigger by navBackStackEntry?.savedStateHandle
        ?.getLiveData<Boolean>("refresh_trigger")
        ?.observeAsState(false) ?: remember { mutableStateOf(false) }

    LaunchedEffect(refreshTrigger) {
        viewModel.loadUserDetail()
        Log.d("DEBUGss", "Refresh trigger ")
        if (refreshTrigger == true) {
            viewModel.loadUserDetail()
            navBackStackEntry?.savedStateHandle?.remove<Boolean>("refresh_trigger")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Image(
            painter = painterResource(R.drawable.buled),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(R.drawable.buledkiri),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { navController.navigate("home") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                    Text(
                        text = "My Profil",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(45.dp))
                }
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Account Info",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.propil),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text (
                            text = userDetails?.email ?: "Loading.....",
                            color = Color.White,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                        Text (
                            text = "********",
                            color = Color.Gray,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { val email = userDetails?. email ?: ""
                                navController.navigate("edituser/$email") }
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Acctivity and Purchase",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(20.dp))

                profileMenuItem(
                    iconRes = R.drawable.ticket,
                    label = "My Tickets",
                    onClick = {navController.navigate("seatselection") }
                )
                Spacer(modifier = Modifier.height(20.dp))

                profileMenuItem(
                    iconRes = R.drawable.loves,
                    label = "My Favorites",
                    onClick = {navController.navigate("favorite") }
                )
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C1A78))
                ) {
                    Text(
                        text = "Sign Out",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(43.dp))
            }
        }
    }

@Composable
fun profileMenuItem(
    iconRes: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal
        )
    }
}