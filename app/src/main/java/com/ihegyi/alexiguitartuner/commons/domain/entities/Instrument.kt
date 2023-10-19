package com.ihegyi.alexiguitartuner.commons.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Instrument (
    @PrimaryKey(autoGenerate = true) val instrumentId : Long,
    val name : String,
    val numberOfStrings : Int
)