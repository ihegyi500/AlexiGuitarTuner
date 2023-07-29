package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tuning (
    @PrimaryKey val tuningId : Long,
    val name : String,
    val instrumentId : Long,
)