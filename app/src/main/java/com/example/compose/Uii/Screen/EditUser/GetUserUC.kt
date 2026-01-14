package com.example.compose.Uii.Screen.EditUser

import com.example.compose.UserDatabase.User
import javax.inject.Inject

class GetUserUC @Inject constructor(
    private val userRepository: EditUserRepository
) {
    suspend operator fun invoke (email: String): User? {
        return userRepository.getUserByEmail(email)
    }
}