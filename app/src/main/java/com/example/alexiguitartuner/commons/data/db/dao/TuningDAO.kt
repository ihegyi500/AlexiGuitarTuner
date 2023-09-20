package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import kotlinx.coroutines.flow.Flow

@Dao
interface TuningDAO {
    @Query("SELECT * FROM Tuning WHERE tuningId = :tuningId")
    suspend fun getTuningById(tuningId: Long?): Tuning
    @Query("SELECT * FROM Tuning WHERE instrumentId = :instrumentId")
    fun getTuningsByInstrument(instrumentId: Long?): Flow<List<Tuning>>

    @Query("SELECT name FROM Tuning t INNER JOIN UserSettings u ON t.tuningId = u.tuningId")
    suspend fun getTuningNameBySettings() : String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTuning(tuning: Tuning)
    @Query("SELECT tuningId FROM Tuning ORDER BY tuningId DESC LIMIT 1")
    suspend fun getLastTuningId(): Long

    @Query("DELETE FROM Tuning WHERE tuningId > 9")
    suspend fun deleteAllCustomTunings()
}