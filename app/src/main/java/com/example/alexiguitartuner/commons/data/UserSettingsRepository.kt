package com.example.alexiguitartuner.commons.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import com.example.alexiguitartuner.feat_metronome.domain.Rhythm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserSettingsRepository @Inject constructor(
    private val appDatabase: AppDatabase
) {
    companion object {
        val initial_user_setting =  UserSettings(
            1,
            1,
            1,
            4,
            100,
            Rhythm.QUARTER,
            useSharp = true,
            useEnglish = true
        )
    }

    fun getUserSettings(): Flow<UserSettings> = appDatabase.userSettingsDAO.getUserSettings()
    suspend fun updateUserSettings(userSettings: UserSettings) = appDatabase.userSettingsDAO.updateUserSettings(userSettings)
    suspend fun getPitches(): List<Pitch> = appDatabase.pitchDAO.getPitches()
    suspend fun updatePitches(pitchList: List<Pitch>) = appDatabase.pitchDAO.updatePitches(pitchList)
    fun getInstruments() = appDatabase.instrumentDAO.getInstruments()
    fun getTuningsByInstrument(id: Long?) = appDatabase.tuningDAO.getTuningsByInstrument(id)
    suspend fun getInstrumentByTuning(tuningId: Long?) = appDatabase.instrumentDAO.getInstrumentByTuningId(tuningId)
    suspend fun getTuningById(tuningId: Long?) = appDatabase.tuningDAO.getTuningById(tuningId)
    suspend fun insertTuning(tuning: Tuning) = appDatabase.tuningDAO.insertTuning(tuning)
    suspend fun insertPitchTuningCrossRef(pitchTuningCrossRef: PitchTuningCrossRef) = appDatabase.pitchTuningCrossRefDAO.insertPitchTuningCrossRef(pitchTuningCrossRef)
    suspend fun getLastTuningId() = appDatabase.tuningDAO.getLastTuningId()
    suspend fun deleteAllCustomTunings() {
        appDatabase.tuningDAO.deleteAllCustomTunings()
        appDatabase.pitchTuningCrossRefDAO.deleteAllCustomTunings()
    }
}