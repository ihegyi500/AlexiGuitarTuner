package com.example.alexiguitartuner.commons.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChordTable (
    @PrimaryKey(autoGenerate = true) val chordTableId : Long,
    val chordId : Long,
    val position : Short,
    val pitchPos : List<Int>
)