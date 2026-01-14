package com.example.compose.Uii.Screen.EditUser

import com.example.compose.UserDatabase.User

interface EditUserRepository {

    suspend fun getUserByEmail(email: String): User?
    suspend fun updateUser(user: User)

}