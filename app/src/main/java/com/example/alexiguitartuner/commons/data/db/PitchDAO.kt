package com.example.alexiguitartuner.commons.data.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PitchDAO {
    @Query("SELECT frequency FROM Pitch WHERE name = :name")
    suspend fun getFrequencyOfPitch(name: String) : Double
}