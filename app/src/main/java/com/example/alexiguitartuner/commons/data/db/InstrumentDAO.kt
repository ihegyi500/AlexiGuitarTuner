package com.example.alexiguitartuner.commons.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.alexiguitartuner.commons.domain.InstrumentWithTuningsAndChords

@Dao
interface InstrumentDAO {

    @Transaction
    @Query("SELECT * FROM Instrument")
    suspend fun getInstrumentsWithTuningsAndChords(): List<InstrumentWithTuningsAndChords>

    @Transaction
    @Query("SELECT * FROM Instrument WHERE :instrumentName LIKE name")
    suspend fun getInstrumentWithTuningsAndChords(instrumentName : String): InstrumentWithTuningsAndChords

    @Query("SELECT name FROM Instrument")
    suspend fun getInstrumentNames(): List<String>
}