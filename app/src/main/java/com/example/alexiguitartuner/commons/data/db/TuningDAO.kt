package com.example.alexiguitartuner.commons.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.alexiguitartuner.commons.domain.TuningWithChords
import com.example.alexiguitartuner.commons.domain.TuningWithPitches

@Dao
interface TuningDAO {

    @Transaction
    @Query("SELECT * FROM Tuning")
    fun getTuningWithPitches(): List<TuningWithPitches>

    @Transaction
    @Query("SELECT * FROM Tuning")
    fun getTuningWithChords(): List<TuningWithChords>

}