package com.ihegyi.alexiguitartuner.commons.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ihegyi.alexiguitartuner.commons.domain.entities.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDAO {
    @Query("SELECT * FROM UserSettings")
    fun getUserSettings(): Flow<UserSettings>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserSettings(userSettings: UserSettings)
}