package com.example.alexiguitartuner.feat_chordtable.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import javax.inject.Inject

class ChordTableRepository @Inject constructor(
    private val appDatabase: AppDatabase
){
    suspend fun getInstrumentsWithCords() = appDatabase.instrumentDAO.getInstrumentWithTunings()
}