package com.example.alexiguitartuner.feat_chordtable.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import javax.inject.Inject

class ChordTableRepository @Inject constructor(
    private val appDatabase: AppDatabase
){
    suspend fun getInstrumentsWithTuningsAndChords() = appDatabase.instrumentDAO.getInstrumentsWithTuningsAndChords()

    suspend fun getInstrumentNames() = appDatabase.instrumentDAO.getInstrumentNames()

    suspend fun getTuningsByInstrument(instrumentName : String) : List<String> {
        val nameList = mutableListOf<String>()
        appDatabase.instrumentDAO.getInstrumentWithTuningsAndChords(instrumentName)
            .tuningWithChordsList.forEach {
                nameList.add(it.tuning.name)
        }
        return nameList
    }

    suspend fun getChordsByTuning(instrumentName : String, tuningName : String) : List<String> {
        val nameList = mutableListOf<String>()
        appDatabase.instrumentDAO.getInstrumentWithTuningsAndChords(instrumentName)
            .tuningWithChordsList.forEach { tuningWithChords ->
                if (tuningWithChords.tuning.name == tuningName) {
                    tuningWithChords.chordList.forEach {
                        nameList.add(it.name)
                    }
                    return@forEach
                }
            }
        return nameList
    }
}