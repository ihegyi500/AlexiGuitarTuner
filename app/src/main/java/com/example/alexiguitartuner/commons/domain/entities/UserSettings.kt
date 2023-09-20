package com.example.alexiguitartuner.commons.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alexiguitartuner.feat_metronome.domain.Rhythm

@Entity
data class UserSettings(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val tuningId : Long,
    val useSharp : Boolean,
    val useEnglish : Boolean
)
