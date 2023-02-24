package com.example.alexiguitartuner.commons.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.alexiguitartuner.commons.domain.ChordWithChordTables
import com.example.alexiguitartuner.commons.domain.ChordWithPitches

@Dao
interface ChordDAO {

    @Transaction
    @Query("SELECT * FROM Chord")
    fun getChordWithChordTables(): List<ChordWithChordTables>

    @Transaction
    @Query("SELECT * FROM Chord")
    fun getChordWithPitches(): List<ChordWithPitches>

}