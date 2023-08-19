package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alexiguitartuner.commons.domain.Pitch
import kotlinx.coroutines.flow.Flow

@Dao
interface PitchDAO {
    @Query("SELECT * FROM Pitch")
    suspend fun getPitches(): List<Pitch>
    @Query("SELECT * FROM Pitch WHERE frequency = :frequency")
    suspend fun getPitch(frequency: Double): Pitch?
    @Query("SELECT * FROM Pitch WHERE name = :name")
    suspend fun getPitchByName(name: String): Pitch?
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePitch(pitch: Pitch)
        @Query("SELECT * FROM Pitch p " +
            "INNER JOIN PitchTuningCrossRef cr ON p.frequency = cr.frequency " +
            "WHERE cr.tuningId = :tuningId" )
    fun getPitchesByTuning(tuningId: Long): Flow<List<Pitch>>
    @Query("SELECT * FROM Pitch p " +
            "INNER JOIN PitchTuningCrossRef cr ON p.frequency = cr.frequency " +
            "INNER JOIN UserSettings u ON u.tuningId = cr.tuningId")
    suspend fun getPitchesByLastTuning(): List<Pitch>
}