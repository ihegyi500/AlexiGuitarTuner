package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alexiguitartuner.commons.domain.entities.Instrument
import kotlinx.coroutines.flow.Flow

@Dao
interface InstrumentDAO {
    @Query("SELECT * FROM Instrument")
    fun getInstruments(): Flow<List<Instrument>>
    @Insert
    suspend fun insertInstruments(instrumentList: List<Instrument>)
    @Query("SELECT * FROM Instrument WHERE instrumentId = :instrumentId")
    suspend fun getInstrumentById(instrumentId : Long?): Instrument
    @Query("SELECT i.* FROM Instrument i INNER JOIN Tuning t ON i.instrumentId = t.instrumentId WHERE t.tuningId = :tuningId")
    suspend fun getInstrumentByTuningId(tuningId : Long?): Instrument
}