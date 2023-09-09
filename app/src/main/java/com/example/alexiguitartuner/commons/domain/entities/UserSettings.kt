package com.example.alexiguitartuner.commons.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alexiguitartuner.feat_metronome.domain.Rhythm

@Entity
data class UserSettings(
    @PrimaryKey(autoGenerate = true) val id : Long,
    // For tuning
    val tuningId : Long,
    // For chord table
    val lastChordTableId : Long,
    // For metronome
    val lastNumOfMetronomeNotes : Int,
    val lastMetronomeBpm : Int,
    val lastMetronomeRhythm : Rhythm,
    // For notation
    val useSharp : Boolean,
    val useEnglish : Boolean
)
