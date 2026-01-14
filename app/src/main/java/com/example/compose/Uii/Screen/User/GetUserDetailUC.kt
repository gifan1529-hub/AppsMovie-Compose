package com.example.compose.Uii.Screen.User

import com.example.compose.SharedPreferences
import com.example.compose.SignIn.SignIn.Domain.Repository.UserRepository
import javax.inject.Inject

data class UserDetails(
    val email : String
)

class GetUserDetailsUC @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(email: String): UserDetails? {
        val userEmail = sharedPreferences.getUserEmail()

        return if (userEmail != null) {
            UserDetails(email = userEmail)
        } else {
            null
        }
    }
}