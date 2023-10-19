package com.ihegyi.alexiguitartuner.feat_chordtable.domain

import com.ihegyi.alexiguitartuner.commons.domain.entities.Chord
import com.ihegyi.alexiguitartuner.commons.domain.entities.ChordTable
import com.ihegyi.alexiguitartuner.commons.domain.entities.Instrument
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import kotlinx.coroutines.flow.Flow

interface ChordTableRepository {
    fun getInstruments(): Flow<List<Instrument>>
    fun getTuningsByInstrument(id: Long?):  Flow<List<Tuning>>
    fun getChordsByTuning(tuningId: Long?): Flow<List<Chord>>
    fun getChordTablesByChord(chordId: Long?): Flow<List<ChordTable>>
    fun getPitchesByTuning(tuningId: Long): Flow<List<Pitch>>
}