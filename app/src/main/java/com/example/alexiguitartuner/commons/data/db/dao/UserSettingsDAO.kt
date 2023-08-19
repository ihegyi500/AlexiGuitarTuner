package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.alexiguitartuner.commons.domain.UserSettings

@Dao
interface UserSettingsDAO {
    @Query("SELECT * FROM UserSettings")
    suspend fun getUserSettings(): UserSettings
    @Update
    suspend fun updateUserSettings(userSettings: UserSettings)
}