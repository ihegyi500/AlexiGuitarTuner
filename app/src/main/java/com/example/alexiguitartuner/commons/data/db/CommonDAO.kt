package com.example.alexiguitartuner.commons.data.db

import androidx.room.*
import com.example.alexiguitartuner.commons.domain.Pitch
import kotlinx.coroutines.flow.Flow

@Dao
interface CommonDAO {

    @Query("SELECT * FROM Pitch")
    fun getPitches(): Flow<List<Pitch>>

    @Query("SELECT * FROM Pitch WHERE name = :name")
    suspend fun getPitchByName(name: String):Pitch?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPitch(pitch:Pitch)

    @Delete
    suspend fun deletePitch(pitch:Pitch)
}