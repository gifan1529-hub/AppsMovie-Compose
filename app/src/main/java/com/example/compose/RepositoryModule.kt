package com.example.compose

import com.example.compose.SignIn.SignIn.Data.UserRepositoryImpl
import com.example.compose.SignIn.SignIn.Domain.Repository.UserRepository
import com.example.compose.Uii.Screen.BookingTicket.BookingHistoryRepository
import com.example.compose.Uii.Screen.BookingTicket.BookingHistoryRepositoryIMPL
import com.example.compose.Uii.Screen.DetailTicket.DetailTicketImpl
import com.example.compose.Uii.Screen.DetailTicket.DetailTicketRepository
import com.example.compose.Uii.Screen.EditUser.EditUserRepository
import com.example.compose.Uii.Screen.EditUser.EditUserRepositoryImpl
import com.example.compose.Uii.Screen.Home.MovieRepository
import com.example.compose.Uii.Screen.Home.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindEditUserRepository(
        editUserRepositoryIMPL: EditUserRepositoryImpl
    ): EditUserRepository

    @Binds
    @Singleton
    abstract fun bindBookingHistoryRepository(
        bookingHistoryRepositoryIMPL: BookingHistoryRepositoryIMPL
    ): BookingHistoryRepository

    @Binds
    @Singleton
    abstract fun bindDetailTicketRepository(
        detailTicketIMPL: DetailTicketImpl
    ): DetailTicketRepository


}