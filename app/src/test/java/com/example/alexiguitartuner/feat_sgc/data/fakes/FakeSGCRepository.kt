package com.example.alexiguitartuner.feat_sgc.data.fakes

import com.example.alexiguitartuner.commons.domain.entities.InstrumentString
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_sgc.domain.ISGCRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSGCRepository : ISGCRepository {
    companion object {
        const val DEFAULT_SCALE_LENGTH = 25.5
        const val DEFAULT_TENSION = 18.0
        const val CONCERT_PITCH = 440.0
    }

    var strings = mutableListOf<InstrumentString>()
    var pitches = mutableListOf<Pitch>()
    override fun getInstrumentStrings(): Flow<List<InstrumentString>> {
        return flow { emit(strings) }
    }

    override suspend fun insertString() {
        strings.add(
            InstrumentString(
                if (strings.isEmpty()) 1 else strings.size + 1,
                CONCERT_PITCH,
                DEFAULT_SCALE_LENGTH,
                DEFAULT_TENSION
            )
        )
    }

    override suspend fun getPitch(frequency: Double): Pitch? {
        println("$pitches")
        return pitches.find { it.frequency == frequency }
    }

    override suspend fun getPitchByName(name: String): Pitch? {
        return pitches.find { it.name == name }
    }

    override suspend fun updateString(string: InstrumentString) {
        strings[string.stringNumber - 1] = string
    }

    override suspend fun deleteString(string: InstrumentString) {
        val stringNumber = string.stringNumber
        strings.removeAt(stringNumber)
        for (i in stringNumber until strings.size) {
            strings[i] = strings[i].copy(stringNumber = i - 1)
        }
    }
}