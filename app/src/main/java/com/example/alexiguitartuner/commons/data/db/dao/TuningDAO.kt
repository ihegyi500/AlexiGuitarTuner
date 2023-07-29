package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.alexiguitartuner.commons.domain.Tuning
import kotlinx.coroutines.flow.Flow

@Dao
interface TuningDAO {
    @Query("SELECT * FROM Tuning WHERE tuningId = :tuningId")
    suspend fun getTuningById(tuningId: Long): Tuning
    @Query("SELECT * FROM Tuning t " +
            "INNER JOIN Instrument i ON t.instrumentId = i.instrumentId " +
            "WHERE i.instrumentId = :instrumentId")
    fun getTuningsOfAnInstrument(instrumentId: Long): Flow<List<Tuning>>

}