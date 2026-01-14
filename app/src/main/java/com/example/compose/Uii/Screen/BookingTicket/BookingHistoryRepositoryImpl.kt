package com.example.compose.Uii.Screen.BookingTicket

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookingHistoryRepositoryIMPL @Inject constructor(
    private val bookingHistoryDao: BookingHistoryDao
) : BookingHistoryRepository {
    override fun getBookingsByEmail(email: String): Flow<List<BookingHistory>> {
        return bookingHistoryDao.getBookingsByEmail(email)
    }
}