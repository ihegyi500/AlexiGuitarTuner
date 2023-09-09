package com.example.alexiguitartuner.feat_chordtable.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import javax.inject.Inject

class ChordTableRepository @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun getInstruments() = appDatabase.instrumentDAO.getInstruments()
    fun getTuningsByInstrument(id: Long?) = appDatabase.tuningDAO.getTuningsByInstrument(id)
    fun getChordsByTuning(tuningId: Long?) = appDatabase.chordDAO.getChordsByTuning(tuningId)
    fun getChordTablesByChord(chordId: Long?) = appDatabase.chordTableDAO.getChordTablesByChord(chordId)
    fun getPitchesByTuning(tuningId: Long) = appDatabase.pitchDAO.getPitchesByTuning(tuningId)
}
