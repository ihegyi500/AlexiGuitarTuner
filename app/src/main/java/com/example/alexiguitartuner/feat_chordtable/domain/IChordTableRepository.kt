package com.example.alexiguitartuner.feat_chordtable.domain

import com.example.alexiguitartuner.commons.domain.entities.Chord
import com.example.alexiguitartuner.commons.domain.entities.ChordTable
import com.example.alexiguitartuner.commons.domain.entities.Instrument
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import kotlinx.coroutines.flow.Flow

interface IChordTableRepository {
    fun getInstruments(): Flow<List<Instrument>>
    fun getTuningsByInstrument(id: Long?):  Flow<List<Tuning>>
    fun getChordsByTuning(tuningId: Long?): Flow<List<Chord>>
    fun getChordTablesByChord(chordId: Long?): Flow<List<ChordTable>>
    fun getPitchesByTuning(tuningId: Long): Flow<List<Pitch>>
}