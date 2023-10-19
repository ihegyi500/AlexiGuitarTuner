package com.ihegyi.alexiguitartuner.commons.data.fakes

import com.ihegyi.alexiguitartuner.commons.domain.UserSettingsRepository
import com.ihegyi.alexiguitartuner.commons.domain.entities.Instrument
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import com.ihegyi.alexiguitartuner.commons.domain.entities.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserSettingsRepository : UserSettingsRepository {
    companion object {
        val initial_user_setting =  UserSettings(
            1,
            1,
            useSharp = true,
            useEnglish = true
        )
    }

    private var userSettings : UserSettings = initial_user_setting
    private val listOfInstruments = mutableListOf<Instrument>()
    private val listOfPitches = mutableListOf<Pitch>()
    private val listOfTunings = mutableListOf<Tuning>()
    private val listOfPitchTuningCrossRef = mutableListOf<PitchTuningCrossRef>()

    init {
        ('a'..'z').forEachIndexed { index, c ->
            listOfInstruments.add(Instrument(index + 1L,"Instrument $c",index + 1))
            listOfTunings.add(Tuning(index + 1L,"Tuning $c",index + 1L))
            listOfPitches.add(Pitch(index.toDouble(), c + index.toString()))
            listOfPitchTuningCrossRef.add(PitchTuningCrossRef(index +1L, listOfPitches.first().frequency))
            listOfPitchTuningCrossRef.add(PitchTuningCrossRef(index +1L, listOfPitches.last().frequency))
        }
    }
    override fun getUserSettings(): Flow<UserSettings> = flow { emit(userSettings) }

    override suspend fun updateUserSettings(userSettings: UserSettings) {
        this.userSettings = userSettings
    }

    override suspend fun getPitches(): List<Pitch> = listOfPitches

    override suspend fun updatePitches(pitchList: List<Pitch>) {
        listOfPitches.forEachIndexed { index, _ ->
            listOfPitches[index] = pitchList[index]
        }
    }

    override fun getInstruments(): Flow<List<Instrument>> = flow { emit(listOfInstruments) }

    override fun getTuningsByInstrument(id: Long?): Flow<List<Tuning>> = flow { emit(listOfTunings.filter { it.instrumentId == id }) }

    override suspend fun getInstrumentByTuning(tuningId: Long?): Instrument {
        return listOfInstruments.find { instrument ->
            listOfTunings.find { tuning ->
                tuning.tuningId == tuningId
            }!!.instrumentId == instrument.instrumentId
        }!!
    }

    override suspend fun getTuningById(tuningId: Long?): Tuning {
        return listOfTunings.find { tuningId == it.tuningId }!!
    }

    override suspend fun insertTuning(tuning: Tuning) {
        listOfTunings.add(tuning)
    }

    override suspend fun insertPitchTuningCrossRef(pitchTuningCrossRef: PitchTuningCrossRef) {
        listOfPitchTuningCrossRef.add(pitchTuningCrossRef)
    }

    override suspend fun getLastTuningId(): Long {
        return listOfTunings.last().tuningId
    }

    override suspend fun deleteAllCustomTunings() {
        listOfTunings.removeIf {
            it.tuningId > 26
        }
        listOfPitchTuningCrossRef.removeIf {
            it.tuningId > 26
        }
    }
}