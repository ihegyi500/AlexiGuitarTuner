package com.example.alexiguitartuner.feat_sgc.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.domain.entities.InstrumentString
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_sgc.domain.SGCRepository
import javax.inject.Inject


class SGCRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : SGCRepository {
    override fun getInstrumentStrings() = appDatabase.stringDAO.getInstrumentStrings()

    override suspend fun insertString() {
        var stringNumber = 1
        if (appDatabase.stringDAO.getCountOfInstrumentStrings() > 0){
            val lastString = appDatabase.stringDAO.getLastElement()
            stringNumber = lastString.stringNumber + 1
        }
        appDatabase.stringDAO.insertString(
            InstrumentString(
                stringNumber,
                440.0,
                25.5,
                18.0
            )
        )
    }
    override suspend fun getPitch(frequency:  Double) : Pitch? = appDatabase.pitchDAO.getPitch(frequency)
    override suspend fun getPitchByName(name: String) : Pitch? = appDatabase.pitchDAO.getPitchByName(name)

    override suspend fun updateString(string: InstrumentString) {
        appDatabase.stringDAO.updateString(string)
    }
    override suspend fun deleteString(string: InstrumentString) {
        val stringNumber = string.stringNumber
        appDatabase.stringDAO.deleteString(string)
        appDatabase.stringDAO.decrementRemainingStringNumbers(stringNumber)
    }
}