package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pitch (
    @PrimaryKey var name: String,
    var frequency: Double
)
