package com.example.alexiguitartuner.feat_sgc.domain

import com.example.alexiguitartuner.commons.domain.entities.InstrumentString
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import kotlinx.coroutines.flow.Flow

interface ISGCRepository {
    fun getInstrumentStrings() : Flow<List<InstrumentString>>
    suspend fun insertString()
    suspend fun getPitch(frequency:  Double) : Pitch?
    suspend fun getPitchByName(name: String) : Pitch?
    suspend fun updateString(string: InstrumentString)
    suspend fun deleteString(string: InstrumentString)
}