package com.example.data

import kotlinx.coroutines.flow.Flow

class HotelRepository(private val hotelDao: HotelDao) {
    val rooms: Flow<List<RoomEntity>> = hotelDao.getAllRoomsFlow()
    val bookings: Flow<List<BookingEntity>> = hotelDao.getAllBookingsFlow()
    val services: Flow<List<ServiceEntity>> = hotelDao.getAllServicesFlow()

    fun getBookingsByEmail(email: String): Flow<List<BookingEntity>> {
        return hotelDao.getBookingsByEmailFlow(email)
    }

    suspend fun getRoomByNumber(roomNumber: String): RoomEntity? {
        return hotelDao.getRoomByNumber(roomNumber)
    }

    suspend fun insertRoom(room: RoomEntity) {
        hotelDao.insertRoom(room)
    }

    suspend fun insertBooking(booking: BookingEntity) {
        hotelDao.insertBooking(booking)
    }

    suspend fun updateBookingStatus(bookingId: Int, status: String) {
        hotelDao.updateBookingStatus(bookingId, status)
    }

    suspend fun updateRoomStatus(roomNumber: String, status: String) {
        hotelDao.updateRoomStatus(roomNumber, status)
    }

    suspend fun insertService(service: ServiceEntity) {
        hotelDao.insertService(service)
    }

    suspend fun updateServiceStatus(serviceId: Int, status: String) {
        hotelDao.updateServiceStatus(serviceId, status)
    }
}
