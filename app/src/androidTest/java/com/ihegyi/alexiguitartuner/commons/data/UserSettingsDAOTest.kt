package com.ihegyi.alexiguitartuner.commons.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.commons.data.db.dao.UserSettingsDAO
import com.ihegyi.alexiguitartuner.commons.domain.entities.UserSettings
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class UserSettingsDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    //SUT
    private lateinit var userSettingsDAO: UserSettingsDAO

    @Before
    fun setUp() {
        hiltRule.inject()
        userSettingsDAO = database.userSettingsDAO
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun updateAndGetUserSettings() = runTest {
        val expectedUserSetting = UserSettings(
            1,
            3,
            useSharp = false,
            useEnglish = false
        )
        userSettingsDAO.updateUserSettings(expectedUserSetting)
        val actualUserSettings = userSettingsDAO.getUserSettings().first()
        assertEquals(expectedUserSetting,actualUserSettings)
    }
}