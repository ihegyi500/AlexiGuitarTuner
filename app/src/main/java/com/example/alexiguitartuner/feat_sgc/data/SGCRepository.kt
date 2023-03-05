package com.example.alexiguitartuner.feat_sgc.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.domain.InstrumentString
import javax.inject.Inject


class SGCRepository @Inject constructor(
    private val appDatabase: AppDatabase
){
    fun getInstrumentStrings() = appDatabase.stringDAO.getInstrumentStrings()

    suspend fun insertString() {
        var stringNumber = 1
        if (appDatabase.stringDAO.getCountOfInstrumentStrings() > 0){
            val lastString = appDatabase.stringDAO.getLastElement()
            stringNumber = lastString.stringNumber + 1
        }
        appDatabase.stringDAO.insertString(
            InstrumentString(
                stringNumber,
                "C0",
                25.5,
                18.0
            )
        )
    }

    suspend fun updateString(string: InstrumentString) {
        appDatabase.stringDAO.updateString(string)
    }

    suspend fun deleteString(string:InstrumentString) {
        val stringNumber = string.stringNumber
        appDatabase.stringDAO.deleteString(string)
        appDatabase.stringDAO.decrementRemainingStringNumbers(stringNumber)
    }

}