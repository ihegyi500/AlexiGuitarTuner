package com.ihegyi.alexiguitartuner.commons.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserSettings(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val tuningId : Long,
    val useSharp : Boolean,
    val useEnglish : Boolean
)
