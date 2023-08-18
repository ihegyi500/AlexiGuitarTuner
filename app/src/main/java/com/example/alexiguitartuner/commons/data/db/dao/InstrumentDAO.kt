package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.alexiguitartuner.commons.domain.Instrument
import kotlinx.coroutines.flow.Flow

@Dao
interface InstrumentDAO {
    @Query("SELECT * FROM Instrument")
    fun getInstruments(): Flow<List<Instrument>>

    @Query("SELECT * FROM Instrument WHERE instrumentId = :instrumentId")
    suspend fun getInstrumentById(instrumentId : Long?): Instrument

}