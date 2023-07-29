package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity

@Entity(primaryKeys = ["tuningId", "name"])
data class PitchTuningCrossRef(
    val tuningId: Long,
    val name: String
)
