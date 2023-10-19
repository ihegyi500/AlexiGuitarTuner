package com.ihegyi.alexiguitartuner.commons.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ihegyi.alexiguitartuner.commons.data.fakes.FakeUserSettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTuningViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    //SUT
    private lateinit var createTuningViewModel: CreateTuningViewModel
    //FAKES
    private lateinit var fakeUserSettingsRepository: FakeUserSettingsRepository
    @Before
    fun setUp() {
        fakeUserSettingsRepository = FakeUserSettingsRepository()
        createTuningViewModel = CreateTuningViewModel(
            UnconfinedTestDispatcher(),
            fakeUserSettingsRepository
        )
    }

    @Test
    fun changeSelectedInstrument() = runTest {
        var expectedInstrument = fakeUserSettingsRepository.getInstruments().first().first()
        var actualInstrument = createTuningViewModel.uiState.first().selectedInstrument
        assertEquals(expectedInstrument,actualInstrument)
        createTuningViewModel.changeSelectedInstrument(fakeUserSettingsRepository.getInstruments().first().size - 1)
        expectedInstrument = fakeUserSettingsRepository.getInstruments().first().last()
        actualInstrument = createTuningViewModel.uiState.first().selectedInstrument
        assertEquals(expectedInstrument,actualInstrument)
    }

    @Test
    fun updatePitchInList() = runTest {
        val expectedChangedPitch = createTuningViewModel.pitchList.first()
        var actualChangedPitch = createTuningViewModel.uiState.first().pitchesOfTuning[5]
        assertNotEquals(expectedChangedPitch,actualChangedPitch)
        createTuningViewModel.updatePitchInList(5,0)
        actualChangedPitch = createTuningViewModel.uiState.first().pitchesOfTuning[5]
        assertEquals(expectedChangedPitch,actualChangedPitch)
    }

    @Test
    fun insertTuning() = runTest {
        val expectedTuningName = "Custom tuning"
        createTuningViewModel.insertTuning(expectedTuningName)
        val actualTuningName = fakeUserSettingsRepository.getTuningById(0).name
        assertEquals(expectedTuningName,actualTuningName)
    }
}