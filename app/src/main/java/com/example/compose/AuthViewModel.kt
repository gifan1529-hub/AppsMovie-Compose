package com.example.compose

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val prefs: SharedPreferences
) : ViewModel()  {
    fun isLoggedIn(): Boolean {
        return prefs.isLoggedIn()
    }

    fun saveLoginStatus(isLoggedIn: Boolean) {
        prefs.saveLoginStatus(isLoggedIn)
    }
}