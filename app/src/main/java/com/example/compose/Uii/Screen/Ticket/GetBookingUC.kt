package com.example.compose.Uii.Screen.Ticket

import com.example.compose.Uii.Screen.BookingTicket.BookingHistory
import com.example.compose.Uii.Screen.BookingTicket.BookingHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookingUC @Inject constructor(
    private val bookingHistoryRepository: BookingHistoryRepository
) {
    operator fun invoke(email: String): Flow<List<BookingHistory>> {
        return bookingHistoryRepository.getBookingsByEmail(email)
    }
}