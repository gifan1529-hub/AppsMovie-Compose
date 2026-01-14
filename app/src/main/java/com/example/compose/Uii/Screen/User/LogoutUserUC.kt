package com.example.compose.Uii.Screen.User

import com.example.compose.SharedPreferences
import javax.inject.Inject

class LogoutUserUC @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke() {
        sharedPreferences.logoutUser()
    }
}