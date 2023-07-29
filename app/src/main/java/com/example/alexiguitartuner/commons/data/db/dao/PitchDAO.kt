package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alexiguitartuner.commons.domain.Pitch
import kotlinx.coroutines.flow.Flow

@Dao
interface PitchDAO {
    @Query("SELECT * FROM Pitch")
    fun getPitches(): Flow<List<Pitch>>

    @Query("SELECT * FROM Pitch WHERE name = :name")
    suspend fun getPitchByName(name: String): Pitch?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPitch(pitch: Pitch)

    @Delete
    suspend fun deletePitch(pitch: Pitch)

    @Query("SELECT frequency FROM Pitch WHERE name = :name")
    suspend fun getFrequencyOfPitch(name: String) : Double

    @Query("SELECT * FROM Pitch p " +
            "INNER JOIN PitchChordCrossRef cr ON p.name = cr.name " +
            "INNER JOIN Chord c ON c.chordId = cr.chordId " +
            "WHERE c.chordId = :chordId" )
    fun getPitchesOfAChord(chordId: Int): Flow<List<Pitch>>

    @Query("SELECT * FROM Pitch p " +
            "INNER JOIN PitchTuningCrossRef cr ON p.name = cr.name " +
            "INNER JOIN Tuning t ON t.tuningId = cr.tuningId " +
            "WHERE t.tuningId = :tuningId" )
    fun getPitchesOfATuning(tuningId: Long): Flow<List<Pitch>>

    @Query("SELECT * FROM Pitch p " +
            "INNER JOIN PitchTuningCrossRef cr ON p.name = cr.name " +
            "INNER JOIN UserSettings u ON u.tuningId = cr.tuningId")
    fun getPitchesOfLastTuning(): Flow<List<Pitch>>
}