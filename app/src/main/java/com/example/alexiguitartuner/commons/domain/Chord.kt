package com.example.alexiguitartuner.commons.domain

import androidx.room.*
import java.io.Serializable

@Entity
data class Chord(
    @PrimaryKey(autoGenerate = true) val chordId : Int,
    val name : String,
    val instrumentId : Int
)

@Entity(primaryKeys = ["chordId", "name"])
data class PitchChordCrossRef(
    val chordId: Int,
    val name: String
)

data class ChordWithPitches(
    @Embedded val chord: Chord,
    @Relation(
        parentColumn = "chordId",
        entityColumn = "name",
        associateBy = Junction(PitchChordCrossRef::class)
    )
    val pitches: List<Pitch>
)

data class ChordWithChordTables(
    @Embedded val chord: Chord,
    @Relation(
        parentColumn = "chordId",
        entityColumn = "chordId"
    )
    val chordTableList: List<ChordTable>
)