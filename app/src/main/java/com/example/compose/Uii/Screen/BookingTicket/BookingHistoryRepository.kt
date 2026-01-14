package com.example.compose.Uii.Screen.BookingTicket

import kotlinx.coroutines.flow.Flow

interface BookingHistoryRepository {

    fun getBookingsByEmail(email: String): Flow<List<BookingHistory>>
}