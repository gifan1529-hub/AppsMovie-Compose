package com.example.compose.Uii.Screen.Ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.compose.SharedPreferences
import com.example.compose.Uii.Screen.BookingTicket.BookingHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TicketVM @Inject constructor(
    private val getBookingsByEmail : GetBookingUC,
    private val sharedPrefs: SharedPreferences
) : ViewModel() {

    private val userEmail = sharedPrefs.getUserEmail() ?: ""

    val ticketsFlow: Flow<List<BookingHistory>> = getBookingsByEmail(userEmail)

    val ticketsLiveData = getBookingsByEmail(userEmail).asLiveData()
}
