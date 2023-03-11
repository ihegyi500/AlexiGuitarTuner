package com.example.alexiguitartuner.commons.domain

import androidx.room.*

@Entity
data class Tuning (
    @PrimaryKey val tuningId : Long,
    val name : String,
    val instrumentId : Long,
)

@Entity(primaryKeys = ["tuningId", "name"])
data class PitchTuningCrossRef(
    val tuningId: Long,
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

data class TuningWithChords(
    @Embedded val tuning: Tuning,
    @Relation(
        parentColumn = "tuningId",
        entityColumn = "tuningId"
    )
    val chordList: List<Chord>
)