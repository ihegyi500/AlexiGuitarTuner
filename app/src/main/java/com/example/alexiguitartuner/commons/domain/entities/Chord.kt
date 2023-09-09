package com.example.alexiguitartuner.commons.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Chord (
    @PrimaryKey(autoGenerate = true) val chordId : Long,
    val name : String,
    val tuningId : Long
)