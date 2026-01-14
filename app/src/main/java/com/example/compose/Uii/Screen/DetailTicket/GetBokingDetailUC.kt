package com.example.compose.Uii.Screen.DetailTicket

import com.example.compose.Uii.Screen.BookingTicket.BookingHistory
import javax.inject.Inject

class GetBookingDetailUC @Inject constructor(
    private val repository: DetailTicketRepository
) {
    suspend operator fun invoke(id: Int): BookingHistory? {
        return repository.getBookingById(id)
    }
}