package com.example.alexiguitartuner.commons.data.db

import com.example.alexiguitartuner.commons.domain.Pitch
import com.example.alexiguitartuner.commons.domain.UserSettings
import javax.inject.Inject

class UserSettingsRepository@Inject constructor(
    private val appDatabase: AppDatabase
) {
    suspend fun getUserSettings(): UserSettings = appDatabase.userSettingsDAO.getUserSettings()
    suspend fun setUserSettings(userSettings: UserSettings) = appDatabase.userSettingsDAO.updateUserSettings(userSettings)

    suspend fun getPitches(): List<Pitch> = appDatabase.pitchDAO.getPitches()



}