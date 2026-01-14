package com.example.compose.Uii.Screen.BookingTicket

import javax.inject.Inject

class ValidateSeatUC @Inject constructor() {

    operator fun invoke(totalTickets: Int, selectedSeats: Int): Boolean {
        return totalTickets > 0 && selectedSeats == totalTickets
    }
}