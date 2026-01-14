package com.example.compose.Uii.Screen.BookingTicket

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.compose.ui.autofill.ContentDataType
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import java.util.Date
import java.util.Locale
import java.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.format

data class BookingData(
    var movieId: String? = null,
    var movieTitle: String? = null,
    var moviePosterUrl: String? = null,
    var movieGenre: String? = null,
    var theater: String? = null,
    var session: String? = null,
    var adultTickets: Int = 0,
    var childTickets: Int = 0,
    var buffetSubtotal: Double = 0.0,
    var selectedSeats: MutableSet<Int> = mutableSetOf(),
    var selectedBuffet: String? = "None",
    var totalPrice: Double = 0.0
)
@HiltViewModel
class BookingTicketVM @Inject constructor (
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context,
    private val BookingDao: BookingHistoryDao,
    private val validateSeatUC: ValidateSeatUC,
    private val calculatePriceUC: CalculatePriceUC,
    private val buffetMenuUC: BuffetMenuUC
) : ViewModel() {
    private val _lastInsertedId = MutableLiveData<Int?>(null)
    val lastInsertedId: LiveData<Int?> = _lastInsertedId

    private val _areSeatsValid = MutableLiveData<Boolean>(false)
    val areSeatsValid: LiveData<Boolean> get() = _areSeatsValid

    private val _isTheaterSelected = MutableLiveData<Boolean>(false)
    val isTheaterSelected: LiveData<Boolean> get() = _isTheaterSelected

    private val _isSessionSelected = MutableLiveData<Boolean>(false)
    val isSessionSelected: LiveData<Boolean> get() = _isSessionSelected

    private val _buffetMenuList = MutableLiveData<List<BuffetItem>>()
    val buffetMenuList: LiveData<List<BuffetItem>> = _buffetMenuList
    val paymentTrigger = MutableLiveData<Boolean>()
    private val _takenSeats = MutableLiveData<Set<String>>()
    val takenSeats: LiveData<Set<String>> = _takenSeats
    private val _bookingData = MutableLiveData<BookingData>()
    val bookingData: LiveData<BookingData> = _bookingData

    init {
        _bookingData.value = BookingData()
        _buffetMenuList.value = buffetMenuUC()
    }

    fun onConfirmPaymentClicked() {
        paymentTrigger.value = true
    }

    fun onPaymentFinished() {
        paymentTrigger.value = false
    }

    private fun updateStateAndValidate(){
        val currentData = _bookingData.value ?: return

        val newTotal = calculatePriceUC(
            currentData.adultTickets,
            currentData.childTickets,
            _buffetMenuList.value ?: emptyList()
        )

        val updatedData = currentData.copy(totalPrice = newTotal)
        _bookingData.value = updatedData
    }

    private fun updateBuffetAndRecalculate() {
        val currentData = _bookingData.value ?: return
        var buffetTotal = 0.0
        _buffetMenuList.value?.forEach { buffetItem ->
            buffetTotal += buffetItem.price * buffetItem.quantity
        }
        currentData.buffetSubtotal = buffetTotal
        updateStateAndValidate()
    }

    fun removeBuffetItem(item: BuffetItem) {
        val currentList = _buffetMenuList.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1 && currentList[index].quantity > 0) {
            val updatedItem = currentList[index].copy(quantity = currentList[index].quantity - 1)
            currentList[index] = updatedItem

            _buffetMenuList.value = currentList
            updateBuffetAndRecalculate()
        }
    }

    fun addBuffetItem(item: BuffetItem) {
        val currentList = _buffetMenuList.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == item.id }

        if (index != -1) {
            val updatedItem = currentList[index].copy(quantity = currentList[index].quantity + 1)
            currentList[index] = updatedItem

            _buffetMenuList.value = currentList
            updateBuffetAndRecalculate()
        }
    }

    fun addAdultTicket() {
        val currentData = _bookingData.value ?: return
        currentData.adultTickets++
        updateStateAndValidate()
    }

    fun removeAdultTicket() {
        val currentData = _bookingData.value ?: return
        if (currentData.adultTickets > 0) {
            currentData.adultTickets--
            updateStateAndValidate()
        }
    }

    fun addChildTicket() {
        val currentData = _bookingData.value ?: return
        currentData.childTickets++
        updateStateAndValidate()
    }

    fun removeChildTicket() {
        val currentData = _bookingData.value ?: return
        if (currentData.childTickets > 0) {
            currentData.childTickets--
            updateStateAndValidate()
        }
    }

    fun confirmBuffetSelection() {
        val currentBookingData = _bookingData.value ?: return
        val selectedBuffetItems = _buffetMenuList.value?.filter { it.quantity > 0 }
        currentBookingData.selectedBuffet = selectedBuffetItems?.joinToString("\n") {
            "${it.quantity}x ${it.name}"
        }?.takeIf { it.isNotBlank() } ?: "None"
        _bookingData.postValue(currentBookingData)
    }

    fun setInitialMovieData(id: String?, title: String?, posterUrl: String?, genre: String?) {
        _bookingData.value = BookingData(
            movieId = id,
            movieTitle = title,
            moviePosterUrl = posterUrl,
            movieGenre = genre
        )
    }

    fun setTheater(theaterName: String) {
        val currentData = _bookingData.value ?: return
        currentData.theater = theaterName
        _bookingData.postValue(currentData)
        _isTheaterSelected.value = true
    }


    fun setSession(sessionTime: String) {
        val currentData = _bookingData.value ?: return
        currentData.session = sessionTime
        _bookingData.postValue(currentData)
        _isSessionSelected.value = true
    }

    fun onSeatSelected(seatIndex: Int) {
        val currentData = _bookingData.value ?: return
        val totalTickets = currentData.adultTickets + currentData.childTickets
        val newSelectedSeats = currentData.selectedSeats.toMutableSet()
        if (newSelectedSeats.contains(seatIndex)) {
            newSelectedSeats.remove(seatIndex)
        } else {
            if (newSelectedSeats.size < totalTickets) {
                newSelectedSeats.add(seatIndex)
            }
        }
        _bookingData.value = currentData.copy(selectedSeats = newSelectedSeats)
    }

    fun resetBookingData() {
        _bookingData.value = BookingData()
        _isSessionSelected.value = false
        _isTheaterSelected.value = false
        _areSeatsValid.value = false
    }

    fun fetchTakenSeats(movieId: String, theater: String, session: String) {
        viewModelScope.launch {
            val listOfSeatLists = BookingDao.getTakenSeatsForShow(movieId, theater, session)
            val allTakenSeats = listOfSeatLists.flatMap { it.split(",") }.toSet()
            _takenSeats.postValue(allTakenSeats)
        }
    }

    fun confirmPaymentAndSave(method: String, userEmail: String) {
        val data = _bookingData.value ?: return

        viewModelScope.launch {
            try {
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val currentDate = sdf.format(Date())

                val history = BookingHistory(
                    movieTitle = data.movieTitle ?: "Unknown Movie",
                    theater = data.theater ?: "Unknown Theater",
                    session = data.session ?: "Unknown Session",
                    seatIds = data.selectedSeats.toList().map { it.toString() },
                    totalPrice = data.totalPrice.toLong(),
                    paymentMethod = method,
                    paymentStatus = "LUNAS",
                    id = 0,
                    email = userEmail,
                    buffetItems = data.selectedBuffet ?: "None",
                    adultTickets = data.adultTickets,
                    childTickets = data.childTickets,
                    moviePosterUrl = data.moviePosterUrl ?: "",
                    bookingDate = currentDate
                )

                val newId = BookingDao.insertBooking(history)
                _lastInsertedId.postValue(newId.toInt())

//                BookingDao.insertBooking(history)

                showSuccessNotification(
                    movieTitle = data.movieTitle ?: "",
                    theater = data.theater ?: "",
                    bookingId = newId.toInt()
                )

                onPaymentFinished()

//                resetBookingData()

            } catch (e: Exception) {
                paymentTrigger.value = false
                Log.e("BookingVM", "Failed to save booking: ${e.message}")
            }
        }
    }

    fun clearLastInsertedId() {
        _lastInsertedId.value = null
    }

    private fun showSuccessNotification(movieTitle: String, theater: String, bookingId: Int) {
        Log.d("Notification", "${movieTitle}")
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val routeUri = "app://movie/detailticket/$bookingId".toUri()

        val intent = Intent(
            Intent.ACTION_VIEW,
            routeUri,
            context,
            MainActivity::class.java
        )

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                bookingId,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(context, "PYAMENT_SUCCES_CHANNEL")
            .setSmallIcon(com.example.compose.R.drawable.ticket)
            .setContentTitle("Pembayaran Berhasil! 🍿")
            .setContentText("Tiket untuk $movieTitle di $theater berhasil dipesan.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}