package com.example.alexiguitartuner.commons.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.alexiguitartuner.commons.domain.InstrumentWithChords
import com.example.alexiguitartuner.commons.domain.InstrumentWithTunings

@Dao
interface InstrumentDAO {

    @Transaction
    @Query("SELECT * FROM Instrument")
    fun getInstrumentWithChords(): List<InstrumentWithChords>

    @Transaction
    @Query("SELECT * FROM Instrument")
    fun getInstrumentWithTunings(): List<InstrumentWithTunings>

}