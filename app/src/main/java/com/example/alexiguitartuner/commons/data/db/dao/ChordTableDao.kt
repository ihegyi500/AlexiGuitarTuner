package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.alexiguitartuner.commons.domain.ChordTable
import kotlinx.coroutines.flow.Flow

@Dao
interface ChordTableDao {

    @Query("SELECT position FROM ChordTable WHERE chordTableId = :chordTableId")
    suspend fun getPositionOfChordTable(chordTableId : Long): Int

    @Query("SELECT * FROM ChordTable ct INNER JOIN Chord c ON ct.chordId = c.chordId WHERE c.chordId = :chordId")
    fun getChordTablesOfAChord(chordId: Int): Flow<List<ChordTable>>
}