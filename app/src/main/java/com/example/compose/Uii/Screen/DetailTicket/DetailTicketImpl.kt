package com.example.compose.Uii.Screen.DetailTicket

import com.example.compose.Uii.Screen.BookingTicket.BookingHistory
import com.example.compose.Uii.Screen.BookingTicket.BookingHistoryDao
import javax.inject.Inject

class DetailTicketImpl @Inject constructor(
    private val bookingHistoryDao: BookingHistoryDao
) : DetailTicketRepository {

    override suspend fun getBookingById(id: Int): BookingHistory? {
        return bookingHistoryDao.getBookingById(id)
    }
}