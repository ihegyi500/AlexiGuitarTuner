package com.ihegyi.alexiguitartuner.commons.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChordTable (
    @PrimaryKey(autoGenerate = true) val chordTableId : Long,
    val chordId : Long,
    val position : Int,
    val pitchPos : List<Int>
)