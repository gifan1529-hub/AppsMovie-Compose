package com.example.compose.Uii.Screen.User

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.SharedPreferences
import com.example.compose.UserDatabase.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVM @Inject constructor(
    private val getUser: GetUserDetailsUC,
    private val logoutUser: LogoutUserUC,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _userDetails = MutableLiveData<UserDetails?>()
    val userDetails: LiveData<UserDetails?> = _userDetails

    private val _logoutEvent = MutableLiveData<Boolean>()
    val logoutEvent: LiveData<Boolean> = _logoutEvent

    fun loadUserDetail(){
        viewModelScope.launch {
            delay(100)
            val email = sharedPreferences.getUserEmail() ?: ""
            Log.d("DEBUGss", "Loading data for email: $email")
            val details = getUser(email)
            _userDetails.postValue(details)
        }
    }
    fun logout() {
        viewModelScope.launch {
            logoutUser()
            _logoutEvent.postValue(true)
        }
    }
}