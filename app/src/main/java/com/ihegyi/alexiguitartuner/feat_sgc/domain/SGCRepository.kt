package com.ihegyi.alexiguitartuner.feat_sgc.domain

import com.ihegyi.alexiguitartuner.commons.domain.entities.InstrumentString
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import kotlinx.coroutines.flow.Flow

interface SGCRepository {
    fun getInstrumentStrings() : Flow<List<InstrumentString>>
    suspend fun insertString()
    suspend fun getPitch(frequency:  Double) : Pitch?
    suspend fun getPitchByName(name: String) : Pitch?
    suspend fun updateString(string: InstrumentString)
    suspend fun deleteString(string: InstrumentString)
}