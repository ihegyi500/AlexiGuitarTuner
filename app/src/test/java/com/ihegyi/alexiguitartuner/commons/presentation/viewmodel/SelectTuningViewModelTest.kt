package com.ihegyi.alexiguitartuner.commons.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ihegyi.alexiguitartuner.commons.data.fakes.FakeUserSettingsRepository
import com.ihegyi.alexiguitartuner.commons.domain.entities.Instrument
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SelectTuningViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //SUT
    private lateinit var selectTuningViewModel: SelectTuningViewModel
    //FAKES
    private lateinit var fakeUserSettingsRepository: FakeUserSettingsRepository
    @Before
    fun setUp() {
        fakeUserSettingsRepository = FakeUserSettingsRepository()
        selectTuningViewModel = SelectTuningViewModel(
            UnconfinedTestDispatcher(),
            fakeUserSettingsRepository
        )
    }

    @Test
    fun initTuningList() = runTest {
        val expectedSelectedTuningId = FakeUserSettingsRepository.initial_user_setting.tuningId
        selectTuningViewModel.initTuningList()
        val actualSelectedTuningId = selectTuningViewModel.uiState.first().selectedTuning?.tuningId
        assertEquals(expectedSelectedTuningId,actualSelectedTuningId)
    }

    @Test
    fun selectInstrument() = runTest {
        val expectedSelectedInstrument = Instrument(26,"Instrument Z",26)
        selectTuningViewModel.selectInstrument(expectedSelectedInstrument)
        val actualSelectedInstrument = selectTuningViewModel.uiState.first().selectedInstrument
        assertEquals(expectedSelectedInstrument,actualSelectedInstrument)
    }

    @Test
    fun selectTuning() = runTest {
        val expectedSelectedTuning = Tuning(26,"Tuning Z",26)
        selectTuningViewModel.selectTuning(expectedSelectedTuning)
        val actualSelectedTuning = selectTuningViewModel.uiState.first().selectedTuning
        assertEquals(expectedSelectedTuning,actualSelectedTuning)
    }

    @Test
    fun updateSelectedTuning() = runTest {
        val expectedSelectedTuning = Tuning(26,"Tuning Z",26)
        selectTuningViewModel.selectTuning(expectedSelectedTuning)
        selectTuningViewModel.updateSelectedTuning()
        val actualSelectedTuning = fakeUserSettingsRepository.getUserSettings().first()
        assertEquals(expectedSelectedTuning.tuningId,actualSelectedTuning.tuningId)
    }
}