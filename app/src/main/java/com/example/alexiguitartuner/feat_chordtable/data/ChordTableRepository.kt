package com.example.alexiguitartuner.feat_chordtable.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import javax.inject.Inject

class ChordTableRepository @Inject constructor(
    private val appDatabase: AppDatabase
){/*
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
            .tuningWithChordsList.find { it.tuning.name == tuningName }
            ?.chordList?.forEach { nameList.add(it.chord.name) }

        return nameList
    }

    suspend fun getChordTablesByPosition(instrumentName : String, tuningName : String, chordName : String)
    : MutableMap<Short,List<Int>> {
        val chordTables : MutableMap<Short,List<Int>> = mutableMapOf()
        appDatabase.instrumentDAO.getInstrumentWithTuningsAndChords(instrumentName)
            .tuningWithChordsList.find { it.tuning.name == tuningName }
            ?.chordList?.find { it.chord.name == chordName }
            ?.chordTableList?.forEach {
                chordTables.put(it.position,it.pitchPos)
            }
        return chordTables
    }
    */
}