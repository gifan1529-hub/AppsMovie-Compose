package com.example.compose.UserDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.compose.ApiOffline.RoomDao
import com.example.compose.ApiOffline.RoomApi
import com.example.compose.Uii.Screen.Favorite.FavoriteMovie
import com.example.compose.Converter
import com.example.compose.Uii.Screen.BookingTicket.BookingHistory
import com.example.compose.Uii.Screen.BookingTicket.BookingHistoryDao
import com.example.compose.Uii.Screen.Favorite.MovieDao
import com.example.compose.UserDatabase.User
import com.example.compose.UserDatabase.UserDao

@TypeConverters(Converter::class)
@Database(entities = [
    User::class,
    RoomApi::class,
    FavoriteMovie::class,
    BookingHistory::class
], version = 5,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun roomDao() : RoomDao
    abstract fun movieDao() : MovieDao
    abstract fun bookingHistoryDao(): BookingHistoryDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context : Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }
}