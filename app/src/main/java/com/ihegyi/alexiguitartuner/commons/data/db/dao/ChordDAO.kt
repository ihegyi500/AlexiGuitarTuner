package com.ihegyi.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ihegyi.alexiguitartuner.commons.domain.entities.Chord
import kotlinx.coroutines.flow.Flow

@Dao
interface ChordDAO {
    @Query("SELECT * FROM Chord WHERE chordId = :chordId")
    suspend fun getChordById(chordId: Long?): Chord
    @Query("SELECT * FROM Chord WHERE tuningId = :tuningId" )
    fun getChordsByTuning(tuningId: Long?): Flow<List<Chord>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChord(chord: Chord)
}