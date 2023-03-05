package com.example.alexiguitartuner.commons.data.db

import androidx.room.*
import com.example.alexiguitartuner.commons.domain.InstrumentString
import com.example.alexiguitartuner.commons.domain.Pitch
import kotlinx.coroutines.flow.Flow

@Dao
interface StringDAO {

    @Query("SELECT * FROM InstrumentString")
    fun getInstrumentStrings(): Flow<List<InstrumentString>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertString(instrumentString: InstrumentString)

    @Update
    suspend fun updateString(instrumentString: InstrumentString)

    @Delete
    suspend fun deleteString(string: InstrumentString)

    @Query("SELECT * FROM InstrumentString ORDER BY stringNumber DESC LIMIT 1")
    fun getLastElement(): InstrumentString

    @Query("SELECT COUNT(*) FROM InstrumentString")
    fun getCountOfInstrumentStrings():Int

    @Query("UPDATE InstrumentString SET name = :name WHERE stringNumber = :stringNumber")
    suspend fun setName(stringNumber: Int, name: String)

    @Query("UPDATE InstrumentString SET scaleLength = :scaleLength WHERE stringNumber = :stringNumber")
    suspend fun setScaleLength(stringNumber: Int, scaleLength: Double)

    @Query("UPDATE InstrumentString SET tension = :tension WHERE stringNumber = :stringNumber")
    suspend fun setTension(stringNumber: Int, tension: Double)

    @Query("UPDATE InstrumentString SET stringNumber = stringNumber - 1 WHERE stringNumber > :stringNumber")
    suspend fun decrementRemainingStringNumbers(stringNumber: Int)

}