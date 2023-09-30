package com.example.alexiguitartuner.feat_chordtable.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.feat_chordtable.domain.IChordTableRepository
import javax.inject.Inject

class ChordTableRepository @Inject constructor(
    private val appDatabase: AppDatabase
) : IChordTableRepository {
    override fun getInstruments() = appDatabase.instrumentDAO.getInstruments()
    override fun getTuningsByInstrument(id: Long?) = appDatabase.tuningDAO.getTuningsByInstrument(id)
    override fun getChordsByTuning(tuningId: Long?) = appDatabase.chordDAO.getChordsByTuning(tuningId)
    override fun getChordTablesByChord(chordId: Long?) = appDatabase.chordTableDAO.getChordTablesByChord(chordId)
    override fun getPitchesByTuning(tuningId: Long) = appDatabase.pitchDAO.getPitchesByTuning(tuningId)
}
