package com.ihegyi.alexiguitartuner.commons.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tuning (
    @PrimaryKey(autoGenerate = true) val tuningId : Long,
    val name : String,
    val instrumentId : Long,
)