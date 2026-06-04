package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelDao {
    // Rooms Queries
    @Query("SELECT * FROM rooms ORDER BY roomNumber ASC")
    fun getAllRoomsFlow(): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms")
    suspend fun getAllRooms(): List<RoomEntity>

    @Query("SELECT * FROM rooms WHERE roomNumber = :roomNumber")
    suspend fun getRoomByNumber(roomNumber: String): RoomEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(rooms: List<RoomEntity>)

    @Update
    suspend fun updateRoom(room: RoomEntity)

    @Query("UPDATE rooms SET status = :status WHERE roomNumber = :roomNumber")
    suspend fun updateRoomStatus(roomNumber: String, status: String)

    // Bookings Queries
    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getAllBookingsFlow(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE guestEmail = :email ORDER BY timestamp DESC")
    fun getBookingsByEmailFlow(email: String): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    suspend fun updateBookingStatus(bookingId: Int, status: String)

    // Services Queries
    @Query("SELECT * FROM services ORDER BY timestamp DESC")
    fun getAllServicesFlow(): Flow<List<ServiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity)

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Query("UPDATE services SET status = :status WHERE id = :serviceId")
    suspend fun updateServiceStatus(serviceId: Int, status: String)
}
