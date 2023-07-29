package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Instrument (
    @PrimaryKey val instrumentId : Long,
    val name : String,
    val numberOfStrings : Int
)