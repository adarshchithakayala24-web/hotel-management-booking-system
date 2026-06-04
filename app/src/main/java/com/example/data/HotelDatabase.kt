package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [RoomEntity::class, BookingEntity::class, ServiceEntity::class], version = 1, exportSchema = false)
abstract class HotelDatabase : RoomDatabase() {
    abstract fun hotelDao(): HotelDao

    companion object {
        @Volatile
        private var INSTANCE: HotelDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): HotelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HotelDatabase::class.java,
                    "hotel_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Populate rooms asynchronously on first launch
                        scope.launch(Dispatchers.IO) {
                            prepopulateRooms(getDatabase(context, scope).hotelDao())
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulateRooms(dao: HotelDao) {
            val initialRooms = listOf(
                RoomEntity(
                    roomNumber = "101",
                    type = "Classic Standard",
                    price = 120.00,
                    status = "Available",
                    rating = 4.2,
                    features = "Queen Bed, Wi-Fi, Smart TV, Rainforest Shower, Workspace Desk",
                    imageUrl = "https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=500&q=80"
                ),
                RoomEntity(
                    roomNumber = "102",
                    type = "Classic Standard",
                    price = 135.00,
                    status = "Cleaning",
                    rating = 4.4,
                    features = "Twin Beds, Wi-Fi, Garden View, Coffee Station, Rainforest Shower",
                    imageUrl = "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?auto=format&fit=crop&w=500&q=80"
                ),
                RoomEntity(
                    roomNumber = "201",
                    type = "Grand Deluxe",
                    price = 240.00,
                    status = "Available",
                    rating = 4.7,
                    features = "King Bed, Balcony, Ocean View, Mini-Bar, Smart TV, Rain Shower, Free Espresso",
                    imageUrl = "https://images.unsplash.com/photo-1591088398332-8a7791972843?auto=format&fit=crop&w=500&q=80"
                ),
                RoomEntity(
                    roomNumber = "202",
                    type = "Grand Deluxe",
                    price = 260.00,
                    status = "Occupied",
                    rating = 4.8,
                    features = "King Bed, Jacuzzi Tub, Harbor View, Espresso Machine, Marshall Sound Speaker",
                    imageUrl = "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=500&q=80"
                ),
                RoomEntity(
                    roomNumber = "301",
                    type = "Luxury Suite",
                    price = 450.00,
                    status = "Available",
                    rating = 4.95,
                    features = "Penthouse Bed, Private Pool, Panoramic Sea View, Butler Service, Luxury Lounge",
                    imageUrl = "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=500&q=80"
                ),
                RoomEntity(
                    roomNumber = "302",
                    type = "Luxury Suite",
                    price = 490.00,
                    status = "Maintenance",
                    rating = 4.9,
                    features = "Duplex Loft, Sky Garden Access, Personal Spa, Sky Grill, Smart Automated System",
                    imageUrl = "https://images.unsplash.com/photo-1629140727571-9b5c6f6267b4?auto=format&fit=crop&w=500&q=80"
                )
            )
            dao.insertRooms(initialRooms)
        }
    }
}
