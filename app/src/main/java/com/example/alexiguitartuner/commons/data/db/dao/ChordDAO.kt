package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.alexiguitartuner.commons.domain.Chord
import kotlinx.coroutines.flow.Flow

@Dao
interface ChordDAO {
    @Query("SELECT * FROM Chord WHERE chordId = :chordId")
    suspend fun getChordById(chordId: Long): Chord

    @Query("SELECT * FROM Chord c INNER JOIN Tuning t ON c.tuningId = t.tuningId WHERE t.tuningId = :tuningId" )
    fun getChordsOfATuning(tuningId: Long): Flow<List<Chord>>
}