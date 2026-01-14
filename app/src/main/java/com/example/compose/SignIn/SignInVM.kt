package com.example.compose.SignIn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.SharedPreferences
import com.example.compose.SignIn.SignIn.Domain.Repository.UserRepository
import com.example.compose.SignIn.SignIn.Domain.Usecase.LoginResult
import com.example.compose.SignIn.SignIn.Domain.Usecase.LoginUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignInUiState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class SignInVM @Inject constructor(
    private val loginUserUC: LoginUC,
    private val userRepository: UserRepository,
    private val SharedPreferences: SharedPreferences
): ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<LoginResult>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(emailValue = email)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(passwordValue = password)
        }
    }

    fun onSignInClick(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = loginUserUC(_uiState.value.emailValue, _uiState.value.passwordValue)
            if (result is LoginResult.Success) {
                SharedPreferences.saveLoginStatus(true)
                SharedPreferences.saveEmail(_uiState.value.emailValue)
            }
            _eventFlow.emit(result)
            _uiState.update { it.copy(isLoading = false) }

        }
    }
}