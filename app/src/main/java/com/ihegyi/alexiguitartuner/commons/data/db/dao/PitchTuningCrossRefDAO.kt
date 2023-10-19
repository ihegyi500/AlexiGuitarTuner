package com.ihegyi.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ihegyi.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
@Dao
interface PitchTuningCrossRefDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPitchTuningCrossRef(pitchTuningCrossRef: PitchTuningCrossRef)
    @Query("DELETE FROM PitchTuningCrossRef WHERE tuningId > 9")
    suspend fun deleteAllCustomTunings()
}