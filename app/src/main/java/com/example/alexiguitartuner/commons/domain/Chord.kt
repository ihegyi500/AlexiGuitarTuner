package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Chord (
    @PrimaryKey val chordId : Long,
    val name : String,
    val tuningId : Long
)