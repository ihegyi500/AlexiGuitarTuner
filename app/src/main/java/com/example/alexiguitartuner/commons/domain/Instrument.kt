package com.example.alexiguitartuner.commons.domain

import androidx.room.*
import java.io.Serializable

@Entity
data class Instrument(
    @PrimaryKey val instrumentId : Long,
    val name : String,
    val numberOfStrings : Int
)

data class InstrumentWithTunings(
    @Embedded val instrument: Instrument,
    @Relation(
        parentColumn = "instrumentId",
        entityColumn = "instrumentId"
    )
    val tuningList: List<Tuning>
)