package com.ihegyi.alexiguitartuner.feat_chordtable.data

import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.feat_chordtable.domain.ChordTableRepository
import javax.inject.Inject

class ChordTableRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : ChordTableRepository {
    override fun getInstruments() = appDatabase.instrumentDAO.getInstruments()
    override fun getTuningsByInstrument(id: Long?) = appDatabase.tuningDAO.getTuningsByInstrument(id)
    override fun getChordsByTuning(tuningId: Long?) = appDatabase.chordDAO.getChordsByTuning(tuningId)
    override fun getChordTablesByChord(chordId: Long?) = appDatabase.chordTableDAO.getChordTablesByChord(chordId)
    override fun getPitchesByTuning(tuningId: Long) = appDatabase.pitchDAO.getPitchesByTuning(tuningId)
}
