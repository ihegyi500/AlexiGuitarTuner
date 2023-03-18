package com.example.alexiguitartuner.commons.domain

import androidx.room.*
import java.io.Serializable

@Entity
data class Instrument(
    @PrimaryKey val instrumentId : Long,
    val name : String,
    val numberOfStrings : Int
)

data class InstrumentWithTuningsAndChords(
    @Embedded val instrument: Instrument,
    @Relation(
        entity = Tuning::class,
        parentColumn = "instrumentId",
        entityColumn = "instrumentId"
    )
    val tuningWithChordsList: List<TuningWithChords>
)