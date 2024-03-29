package com.ihegyi.alexiguitartuner.commons.data.db.dao

import androidx.room.*
import com.ihegyi.alexiguitartuner.commons.domain.entities.InstrumentString
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
    suspend fun deleteString(instrumentString: InstrumentString)
    @Query("SELECT * FROM InstrumentString ORDER BY stringNumber DESC LIMIT 1")
    suspend fun getLastElement(): InstrumentString
    @Query("SELECT COUNT(*) FROM InstrumentString")
    suspend fun getCountOfInstrumentStrings(): Int
    @Query("UPDATE InstrumentString SET stringNumber = stringNumber - 1 WHERE stringNumber > :stringNumber")
    suspend fun decrementRemainingStringNumbers(stringNumber: Int)
}