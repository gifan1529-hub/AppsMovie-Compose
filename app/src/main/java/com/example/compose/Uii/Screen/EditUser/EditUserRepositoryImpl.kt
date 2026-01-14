package com.example.compose.Uii.Screen.EditUser

import com.example.compose.UserDatabase.User
import com.example.compose.UserDatabase.UserDao
import javax.inject.Inject

class EditUserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : EditUserRepository {
    override suspend fun getUserByEmail(email: String): User? {
        return userDao.get(email)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}