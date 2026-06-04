package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey val roomNumber: String,
    val type: String,           // "Luxury Suite", "Grand Deluxe", "Classic Standard"
    val price: Double,
    val status: String,         // "Available", "Occupied", "Cleaning", "Maintenance"
    val rating: Double,
    val features: String,       // Comma-separated features (e.g., "Ocean View, Wi-Fi, Hot Tub")
    val imageUrl: String
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roomNumber: String,
    val guestName: String,
    val guestEmail: String,
    val checkInDate: String,
    val checkOutDate: String,
    val status: String,         // "Confirmed", "Checked In", "Checked Out", "Cancelled"
    val totalPrice: Double,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roomNumber: String,
    val type: String,           // "Dining", "Housekeeping", "Amenities", "Maintenance"
    val details: String,
    val status: String,         // "Pending", "In Progress", "Completed"
    val timestamp: Long = System.currentTimeMillis()
)
