package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDAO {
    @Query("SELECT * FROM UserSettings")
    fun getUserSettings(): Flow<UserSettings>
    @Update
    suspend fun updateUserSettings(userSettings: UserSettings)
}