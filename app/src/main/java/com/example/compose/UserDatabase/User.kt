package com.example.compose.UserDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true) val uid : Int? = null,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "password") val userPassword : String
)