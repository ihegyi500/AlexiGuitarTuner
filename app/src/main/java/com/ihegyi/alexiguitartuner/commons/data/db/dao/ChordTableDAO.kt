package com.ihegyi.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ihegyi.alexiguitartuner.commons.domain.entities.ChordTable
import kotlinx.coroutines.flow.Flow

@Dao
interface ChordTableDAO {

    @Query("SELECT position FROM ChordTable WHERE chordTableId = :chordTableId")
    suspend fun getPositionOfChordTable(chordTableId : Long): Int

    @Query("SELECT * FROM ChordTable ct WHERE ct.chordId = :chordId")
    fun getChordTablesByChord(chordId: Long?): Flow<List<ChordTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChordTable(chordTable: ChordTable)
}