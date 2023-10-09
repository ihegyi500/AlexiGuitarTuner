package com.example.alexiguitartuner.commons.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.domain.UserSettingsRepository
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserSettingsRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
): UserSettingsRepository {
    companion object {
        val initial_user_setting =  UserSettings(
            1,
            1,
            useSharp = true,
            useEnglish = true
        )
    }

    override fun getUserSettings(): Flow<UserSettings> = appDatabase.userSettingsDAO.getUserSettings()
    override suspend fun updateUserSettings(userSettings: UserSettings) = appDatabase.userSettingsDAO.updateUserSettings(userSettings)
    override suspend fun getPitches(): List<Pitch> = appDatabase.pitchDAO.getPitches()
    override suspend fun updatePitches(pitchList: List<Pitch>) = appDatabase.pitchDAO.updatePitches(pitchList)
    override fun getInstruments() = appDatabase.instrumentDAO.getInstruments()
    override fun getTuningsByInstrument(id: Long?) = appDatabase.tuningDAO.getTuningsByInstrument(id)
    override suspend fun getInstrumentByTuning(tuningId: Long?) = appDatabase.instrumentDAO.getInstrumentByTuningId(tuningId)
    override suspend fun getTuningById(tuningId: Long?) = appDatabase.tuningDAO.getTuningById(tuningId)
    override suspend fun insertTuning(tuning: Tuning) = appDatabase.tuningDAO.insertTuning(tuning)
    override suspend fun insertPitchTuningCrossRef(pitchTuningCrossRef: PitchTuningCrossRef) = appDatabase.pitchTuningCrossRefDAO.insertPitchTuningCrossRef(pitchTuningCrossRef)
    override suspend fun getLastTuningId() = appDatabase.tuningDAO.getLastTuningId()
    override suspend fun deleteAllCustomTunings() {
        appDatabase.tuningDAO.deleteAllCustomTunings()
        appDatabase.pitchTuningCrossRefDAO.deleteAllCustomTunings()
    }
}