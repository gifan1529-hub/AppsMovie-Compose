package com.example.compose.Uii.Screen.DetailTicket

import com.example.compose.Uii.Screen.BookingTicket.BookingHistory

interface DetailTicketRepository {
    suspend fun getBookingById(id: Int): BookingHistory?
}