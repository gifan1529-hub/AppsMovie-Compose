package com.example.compose.SignIn.SignIn.Domain.Repository

import com.example.compose.UserDatabase.User

interface UserRepository {

    suspend fun getUserByEmail(email: String): User?
    suspend fun updateUser(user: User)
    suspend fun findUserByEmail(email: String): User?
    suspend fun insertUser(user: User)

}