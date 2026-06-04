package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BookingEntity
import com.example.data.HotelDatabase
import com.example.data.HotelRepository
import com.example.data.RoomEntity
import com.example.data.ServiceEntity
import com.example.data.GeminiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class UserRole { GUEST, STAFF, NONE }

data class UserSession(
    val email: String = "",
    val name: String = "",
    val role: UserRole = UserRole.NONE
)

class HMSViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = HotelDatabase.getDatabase(application, viewModelScope)
    private val repository = HotelRepository(database.hotelDao())

    // Real-time flows from Room DB
    val rooms: StateFlow<List<RoomEntity>> = repository.rooms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<BookingEntity>> = repository.bookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allServices: StateFlow<List<ServiceEntity>> = repository.services
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Session State
    private val _userSession = MutableStateFlow(UserSession())
    val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

    // Filtered bookings flow based on logged in Guest's email
    val guestBookings: StateFlow<List<BookingEntity>> = _userSession
        .flatMapLatest { session ->
            repository.getBookingsByEmail(session.email)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Concierge Chat State
    private val _chatHistory = MutableStateFlow<List<Pair<String, String>>>(
        listOf(
            "model" to "Welcome to the AuraStay AI Concierge! I am your personal digital butler. I can assist you with local recommendations, dining suggestions, spa schedules, or arranging room cleaning. How may I elevate your luxury stay today?"
        )
    )
    val chatHistory: StateFlow<List<Pair<String, String>>> = _chatHistory.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // Login Action
    fun login(email: String, name: String, role: UserRole) {
        _userSession.value = UserSession(email = email, name = name, role = role)
    }

    // Logout Action
    fun logout() {
        _userSession.value = UserSession()
        // Reset chat history upon logging out
        _chatHistory.value = listOf(
            "model" to "Welcome to the AuraStay AI Concierge! I am your personal digital butler. I can assist you with local recommendations, dining suggestions, spa schedules, or arranging room cleaning. How may I elevate your luxury stay today?"
        )
    }

    // Book Room Action
    fun bookRoom(
        roomNumber: String,
        guestName: String,
        guestEmail: String,
        checkInDate: String,
        checkOutDate: String,
        totalPrice: Double,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val booking = BookingEntity(
                roomNumber = roomNumber,
                guestName = guestName,
                guestEmail = guestEmail,
                checkInDate = checkInDate,
                checkOutDate = checkOutDate,
                status = "Confirmed",
                totalPrice = totalPrice
            )
            repository.insertBooking(booking)
            // Update room status to occupied or confirmed
            repository.updateRoomStatus(roomNumber, "Occupied")
            onSuccess()
        }
    }

    // Cancel Booking Action (Staff or Guest)
    fun cancelBooking(bookingId: Int, roomNumber: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, "Cancelled")
            repository.updateRoomStatus(roomNumber, "Available")
        }
    }

    // Complete Checkout Action
    fun checkOutBooking(bookingId: Int, roomNumber: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, "Checked Out")
            repository.updateRoomStatus(roomNumber, "Cleaning")
        }
    }

    // Toggle Room Status (Staff Administration Panel)
    fun setRoomStatus(roomNumber: String, status: String) {
        viewModelScope.launch {
            repository.updateRoomStatus(roomNumber, status)
        }
    }

    // Request Service / Room dining order (Guest Panel)
    fun requestService(roomNumber: String, type: String, details: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val service = ServiceEntity(
                roomNumber = roomNumber,
                type = type,
                details = details,
                status = "Pending"
            )
            repository.insertService(service)
            onSuccess()
        }
    }

    // Update Service Order Status (Staff Panel)
    fun updateServiceStatus(serviceId: Int, status: String) {
        viewModelScope.launch {
            repository.updateServiceStatus(serviceId, status)
        }
    }

    // Send chat message to AI Concierge
    fun sendConciergeMessage(userMessage: String) {
        if (userMessage.isBlank()) return
        val currentHistory = _chatHistory.value.toMutableList()
        currentHistory.add("user" to userMessage)
        _chatHistory.value = currentHistory
        
        _isChatLoading.value = true
        
        viewModelScope.launch {
            val systemInstruction = """
                You are the AuraStay Executive Digital Concierge, a world-class butler serving discerning guests at AuraStay Luxury Resort & Villas.
                Your tone is highly professional, exceptionally polite, welcoming, elegant, and helpful. You speak with premium grace.
                Use subtle high-hospitality language (e.g., 'A pleasure', 'Discerning stay', 'Rest assured', 'Certainly, Guest').
                You know the following services inside AuraStay:
                - Dining: Prime Tenderloin Steak ($75), Lobster Thermidor ($110), Truffle Pasta ($45). Sourced locally, served 24/7.
                - Spa Services: Located on the Penthouse level. Signature Golden Hour Massage is available from 9 AM to 9 PM daily ($150).
                - Pool: Heated infinity pool on Level 3 open 6 AM to midnight.
                - Housekeeping: Available 24/7. Guests can tap requests for clean linens, extra towels, or turn-down service in the interface.
                Keeps responses structured, clean, informative, and reasonably concise to read easily on a phone.
            """.trimIndent()

            // Prepare list for client sending: omit system prompt or internal formats
            val apiHistory = currentHistory.map { (role, txt) ->
                val apiRole = if (role == "model") "model" else "user"
                apiRole to txt
            }

            val response = GeminiClient.getConciergeResponse(
                systemInstruction = systemInstruction,
                history = apiHistory.dropLast(1), // Use prior history
                userPrompt = userMessage
            )

            val updatedHistory = _chatHistory.value.toMutableList()
            updatedHistory.add("model" to response)
            _chatHistory.value = updatedHistory
            _isChatLoading.value = false
        }
    }
}
