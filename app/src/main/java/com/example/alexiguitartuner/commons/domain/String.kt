package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InstrumentString (
    @PrimaryKey(autoGenerate = false) var stringNumber : Int,
    var name : String,
    var scaleLength : Double,
    var tension : Double
)

