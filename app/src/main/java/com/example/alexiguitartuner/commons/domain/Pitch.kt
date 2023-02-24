package com.example.alexiguitartuner.commons.domain

import androidx.room.*

@Entity
data class Pitch(
    @PrimaryKey val name: String,
    val frequency: Double
)

data class PitchWithStrings(
    @Embedded val pitch: Pitch,
    @Relation(
        parentColumn = "name",
        entityColumn = "name"
    )
    val stringList: List<InstrumentString>
)
