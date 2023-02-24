package com.example.alexiguitartuner.commons.domain

import androidx.room.*

@Entity
data class Tuning (
    @PrimaryKey(autoGenerate = true) val tuningId : Int,
    val name : String,
    val instrumentId : Int
)

@Entity(primaryKeys = ["tuningId", "name"])
data class PitchTuningCrossRef(
    val tuningId: Int,
    val name: String
)

data class TuningWithPitches(
    @Embedded val tuning: Tuning,
    @Relation(
        parentColumn = "tuningId",
        entityColumn = "name",
        associateBy = Junction(PitchTuningCrossRef::class)
    )
    val pitches: List<Pitch>
)
