package com.example.compose.Uii.Screen.EditUser

import com.example.compose.UserDatabase.User
import javax.inject.Inject

class UpdateUserUC @Inject constructor(
    private val userRepository: EditUserRepository
) {
    suspend operator fun invoke(user: User) {
        userRepository.updateUser(user)
    }
}