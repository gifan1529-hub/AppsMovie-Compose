package com.example.compose.SignUp.SignUp.Domain.Usecase

import com.example.compose.SignIn.SignIn.Domain.Repository.UserRepository
import com.example.compose.UserDatabase.User
import javax.inject.Inject

sealed class RegistrationStatus {
    object Success : RegistrationStatus()
    data class Error(val message: String) : RegistrationStatus()
    data class Failure(val error: Exception) : RegistrationStatus()
}

class RegisterUserUC @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke(user: User): RegistrationStatus {
        if (user.email.isBlank()|| user.userPassword.isBlank()) {
            return RegistrationStatus.Error("Semua Field Harus Diisi")
        }
        if (repository.findUserByEmail(user.email) != null) {
            return RegistrationStatus.Error("Email Sudah Terdaftar")
        }

        return try {
            repository.insertUser(user)
            RegistrationStatus.Success
        } catch (e: Exception) {
            RegistrationStatus.Failure(e)
        }
    }
}