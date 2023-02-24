package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity

@Entity
data class InstrumentString (
    val stringNumber : Int,
    val name : String,
    val scaleLength : Double,
    val tension : Double
)

