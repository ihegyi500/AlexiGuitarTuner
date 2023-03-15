package com.example.alexiguitartuner.commons.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface InstrumentDAO {

    /*@Transaction
    @Query("SELECT * FROM Instrument")
    suspend fun getInstrumentWithTuningsAndChords(): List<InstrumentWithTuningsAndChords>
*/
}