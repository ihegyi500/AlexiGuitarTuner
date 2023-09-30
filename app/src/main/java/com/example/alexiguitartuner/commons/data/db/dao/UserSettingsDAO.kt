package com.example.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDAO {
    @Query("SELECT * FROM UserSettings")
    fun getUserSettings(): Flow<UserSettings>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserSettings(userSettings: UserSettings)
}