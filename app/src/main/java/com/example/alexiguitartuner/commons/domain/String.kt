package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InstrumentString (
    @PrimaryKey(autoGenerate = false) val stringNumber : Int,
    val name : String,
    val scaleLength : Double,
    val tension : Double
)

