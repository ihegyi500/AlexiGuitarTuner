package com.example.alexiguitartuner.feat_tuner.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Pitch(
    //@PrimaryKey (autoGenerate = true) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val frequency: Double
) : Serializable