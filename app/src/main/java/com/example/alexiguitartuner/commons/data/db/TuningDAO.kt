package com.example.alexiguitartuner.commons.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.alexiguitartuner.commons.domain.TuningWithPitches

@Dao
interface TuningDAO {

    @Transaction
    @Query("SELECT * FROM Tunings")
    fun getTuningWithPitches(): List<TuningWithPitches>

}