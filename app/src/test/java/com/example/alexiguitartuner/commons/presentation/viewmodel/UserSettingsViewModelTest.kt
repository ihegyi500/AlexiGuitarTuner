package com.example.alexiguitartuner.commons.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.alexiguitartuner.commons.data.fakes.FakeUserSettingsRepository
import com.example.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.feat_chordtable.data.fakes.FakeChordTableRepository
import com.example.alexiguitartuner.feat_chordtable.presentation.viewmodel.ChordTableViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserSettingsViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //SUT
    private lateinit var userSettingsViewModel: UserSettingsViewModel
    //FAKES
    private lateinit var fakeUserSettingsRepository: FakeUserSettingsRepository
    @Before
    fun setUp() {
        fakeUserSettingsRepository = FakeUserSettingsRepository()
        userSettingsViewModel = UserSettingsViewModel(
            UnconfinedTestDispatcher(),
            fakeUserSettingsRepository
        )
    }

    @Test
    fun updateUseSharp() = runTest {
        userSettingsViewModel.updateUseSharp(false)
        val actualUseSharp = userSettingsViewModel.userSettings.first().useSharp
        assertEquals(false,actualUseSharp)
    }

    @Test
    fun updateUseEnglish() = runTest {
        userSettingsViewModel.updateUseEnglish(false)
        val actualUseEnglish = userSettingsViewModel.userSettings.first().useEnglish
        assertEquals(false,actualUseEnglish)
    }

    @Test
    fun deleteAllCustomTunings() = runTest {
        val expectedLastTuningId = fakeUserSettingsRepository.getLastTuningId()
        fakeUserSettingsRepository.insertTuning(
            Tuning(
                expectedLastTuningId + 1,
                "Custom tuning",
                1L
            )
        )
        fakeUserSettingsRepository.insertPitchTuningCrossRef(
            PitchTuningCrossRef(
                expectedLastTuningId + 1,
                0.0
            )
        )
        var actualLastTuningId = fakeUserSettingsRepository.getLastTuningId()
        assertEquals(expectedLastTuningId + 1,actualLastTuningId)
        userSettingsViewModel.deleteAllCustomTunings()
        actualLastTuningId = fakeUserSettingsRepository.getLastTuningId()
        assertEquals(expectedLastTuningId,actualLastTuningId)
    }

    @Test
    fun updateUserSettings() = runTest {
        var expectedPitchName = "H1"
        userSettingsViewModel.updateUseEnglish(false)
        userSettingsViewModel.updateUserSettings()
        var actualPitchName = fakeUserSettingsRepository.getPitches()[11].name
        assertEquals(expectedPitchName,actualPitchName)
        expectedPitchName = "#1"
        userSettingsViewModel.updateUseSharp(true)
        userSettingsViewModel.updateUserSettings()
        actualPitchName = fakeUserSettingsRepository.getPitches()[1].name
        assertEquals(expectedPitchName,actualPitchName)
        expectedPitchName = "b1"
        userSettingsViewModel.updateUseSharp(false)
        userSettingsViewModel.updateUserSettings()
        actualPitchName = fakeUserSettingsRepository.getPitches()[1].name
        assertEquals(expectedPitchName,actualPitchName)
        expectedPitchName = "B1"
        userSettingsViewModel.updateUseEnglish(true)
        userSettingsViewModel.updateUserSettings()
        actualPitchName = fakeUserSettingsRepository.getPitches()[11].name
        assertEquals(expectedPitchName,actualPitchName)
    }
}