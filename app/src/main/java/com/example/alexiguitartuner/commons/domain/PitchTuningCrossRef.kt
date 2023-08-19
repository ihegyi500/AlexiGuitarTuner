package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity

@Entity(primaryKeys = ["tuningId", "frequency"])
data class PitchTuningCrossRef(
    val tuningId: Long,
    val frequency: Double
)
