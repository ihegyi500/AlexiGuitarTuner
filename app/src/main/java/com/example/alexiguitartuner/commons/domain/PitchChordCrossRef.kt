package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity

@Entity(primaryKeys = ["chordId","name"])
data class PitchChordCrossRef(
    val chordId: Long,
    val name: String
)
