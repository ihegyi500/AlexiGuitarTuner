package com.example.alexiguitartuner.commons.domain.entities

import androidx.room.Entity

@Entity(primaryKeys = ["tuningId", "frequency"])
data class PitchTuningCrossRef(
    val tuningId: Long,
    val frequency: Double
)
