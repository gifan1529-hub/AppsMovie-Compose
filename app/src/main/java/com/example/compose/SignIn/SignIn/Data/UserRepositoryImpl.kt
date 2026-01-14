package com.example.compose.SignIn.SignIn.Data

import com.example.compose.SignIn.SignIn.Domain.Repository.UserRepository
import com.example.compose.UserDatabase.User
import com.example.compose.UserDatabase.UserDao
import javax.inject.Inject
import kotlin.text.get

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
): UserRepository{
    override suspend fun getUserByEmail(email: String): User? {
        return userDao.get(email)
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun findUserByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    override suspend fun insertUser(user: User) {
        userDao.insertdata(user)
    }
}