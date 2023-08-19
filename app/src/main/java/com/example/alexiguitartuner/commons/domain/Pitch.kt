package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pitch (
    @PrimaryKey val frequency: Double,
    var name: String
)
