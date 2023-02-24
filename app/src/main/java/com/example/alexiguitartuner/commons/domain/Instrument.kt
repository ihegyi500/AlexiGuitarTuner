package com.example.alexiguitartuner.commons.domain

import androidx.room.*
import java.io.Serializable

@Entity
data class Instrument(
    @PrimaryKey(autoGenerate = true) val instrumentId : Int,
    val name : String,
    val numberOfStrings : Int
)

data class InstrumentWithChords(
    @Embedded val instrument: Instrument,
    @Relation(
        parentColumn = "instrumentId",
        entityColumn = "instrumentId"
    )
    val chordList: List<Chord>
)

data class InstrumentWithTunings(
    @Embedded val instrument: Instrument,
    @Relation(
        parentColumn = "instrumentId",
        entityColumn = "instrumentId"
    )
    val tuningList: List<Tuning>
)