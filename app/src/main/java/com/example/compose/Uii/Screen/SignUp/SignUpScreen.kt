package com.example.compose.Uii.Screen.SignUp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compose.R
import com.example.compose.SignUp.SignUp.Domain.Usecase.RegistrationStatus
import com.example.compose.SignUp.SignUpVM
import com.example.compose.ui.theme.Poppins

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpVM = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collect { result ->
            when (result) {
                is RegistrationStatus.Success -> {
                    Toast.makeText(context, "Silahkan Confirm Account Anda", Toast.LENGTH_SHORT).show()
                    navController.navigate("signin"){
                        popUpTo("signup"){
                            inclusive = true
                        }
                    }
                }
                is RegistrationStatus.Error -> {
                    Toast.makeText(context, "Email Sudah Terdaftar", Toast.LENGTH_SHORT).show()
                }
                is RegistrationStatus.Failure -> {
                    Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.gambar),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            Spacer(modifier = Modifier.weight(2f))

            Image(
                painter = painterResource(R.drawable.movie),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 20.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            TextField(
                value = uiState.emailValue,
                onValueChange = { viewModel.onEmailChange(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("E-Mail", color = Color.Gray) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.DarkGray.copy(alpha = 0.5f),
                    unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.5f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = uiState.passwordValue,
                onValueChange = { viewModel.onPasswordChange(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Password", color = Color.Gray) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.DarkGray.copy(alpha = 0.5f),
                    unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.5f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {viewModel.onSignUpClick()  },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C1A78)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Sign Up",
                    fontSize = 18.sp,
                    fontFamily = Poppins,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    "Do have an account ? ",
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = Poppins,
                    fontSize = 14.sp
                )
                Text(
                    "Sign In",
                    color = Color(0xFF6A49A1),
                    fontWeight = FontWeight.Normal,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("signin")
                        }
                )
            }
            Spacer(modifier = Modifier.weight(1.5f))
        }
    }
}