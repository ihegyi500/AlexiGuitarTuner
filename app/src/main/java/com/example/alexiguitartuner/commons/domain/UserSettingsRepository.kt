package com.example.alexiguitartuner.commons.domain

import com.example.alexiguitartuner.commons.domain.entities.Instrument
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    fun getUserSettings(): Flow<UserSettings>
    suspend fun updateUserSettings(userSettings: UserSettings)
    suspend fun getPitches(): List<Pitch>
    suspend fun updatePitches(pitchList: List<Pitch>)
    fun getInstruments(): Flow<List<Instrument>>
    fun getTuningsByInstrument(id: Long?): Flow<List<Tuning>>
    suspend fun getInstrumentByTuning(tuningId: Long?): Instrument
    suspend fun getTuningById(tuningId: Long?): Tuning
    suspend fun insertTuning(tuning: Tuning)
    suspend fun insertPitchTuningCrossRef(pitchTuningCrossRef: PitchTuningCrossRef)
    suspend fun getLastTuningId(): Long
    suspend fun deleteAllCustomTunings()
}