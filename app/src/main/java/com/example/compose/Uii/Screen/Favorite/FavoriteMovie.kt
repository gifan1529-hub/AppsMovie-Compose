package com.example.compose.Uii.Screen.Favorite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies_table",
    primaryKeys = ["id", "email"] )
data class FavoriteMovie (
    val id: String,
    val email: String,
    val title: String?,
    val posterUrl: String?,
    val plot: String?,
    val rating: Double?
)
