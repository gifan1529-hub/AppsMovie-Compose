package com.example.compose

import android.content.Context
import androidx.room.Room
import com.example.compose.Api.ApiClient
import com.example.compose.Api.ApiService
import com.example.compose.ApiOffline.RoomDao
import com.example.compose.Uii.Screen.BookingTicket.BookingHistoryDao
import com.example.compose.Uii.Screen.Home.MovieDao
import com.example.compose.UserDatabase.AppDatabase
import com.example.compose.UserDatabase.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "user_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return SharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideRoomDao(appDatabase: AppDatabase): RoomDao {
        return appDatabase.roomDao()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiClient.create()
    }

    @Provides
    @Singleton
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Provides
    @Singleton
    fun provideBookingHistoryDao(appDatabase: AppDatabase): BookingHistoryDao {
        return appDatabase.bookingHistoryDao()
    }
}