package com.example.alexiguitartuner.feat_tuner.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_tuner.data.fakes.FakeButtonGenerationRepository
import com.example.alexiguitartuner.feat_tuner.data.fakes.FakePitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.data.fakes.FakePitchGenerationRepository
import com.example.alexiguitartuner.feat_tuner.presentation.state.PitchButtonsUIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TunerViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //SUT
    private lateinit var tunerViewModel: TunerViewModel
    //FAKES
    private lateinit var fakeButtonGenerationRepository: FakeButtonGenerationRepository
    private lateinit var fakePitchGenerationRepository: FakePitchGenerationRepository
    private lateinit var fakePitchDetectionRepository: FakePitchDetectionRepository
    @Before
    fun setUp() {
        fakeButtonGenerationRepository = FakeButtonGenerationRepository()
        fakePitchGenerationRepository = FakePitchGenerationRepository()
        fakePitchDetectionRepository = FakePitchDetectionRepository()

        tunerViewModel = TunerViewModel(
            UnconfinedTestDispatcher(),
            fakePitchGenerationRepository,
            fakePitchDetectionRepository,
            fakeButtonGenerationRepository
        )
    }
    @Test
    fun initPitchButtons() = runTest {
        val expectedPitch = PitchButtonsUIState(
            tuningName = "Tuning A",
            pitchList = listOf(
                Pitch(0.0, "A0"),
                Pitch(1.0, "B1"),
                Pitch(2.0, "C2"),
                Pitch(3.0, "D3")
            )
        )
        tunerViewModel.initPitchButtons()
        assertEquals(expectedPitch,tunerViewModel.pitchButtonsUIState.first())
    }

    @Test
    fun init() = runTest {
        fakePitchDetectionRepository.initPitchList()
        assertNotEquals(emptyList<Pitch>(),fakePitchDetectionRepository.pitchList)
    }

    @Test
    fun startAudioProcessing() = runTest {
        tunerViewModel.startAudioProcessing()
        assertEquals(true,fakePitchDetectionRepository.jobState)
    }

    @Test
    fun stopAudioProcessing() = runTest {
        tunerViewModel.stopAudioProcessing()
        assertEquals(true,fakePitchDetectionRepository.jobState)
    }

    @Test
    fun startPitchGeneration() = runTest {
        tunerViewModel.startPitchGeneration(400.0)
        assertEquals(true,fakePitchGenerationRepository.jobState)
    }

    @Test
    fun stopPitchGeneration() = runTest {
        tunerViewModel.stopPitchGeneration()
        assertEquals(true,fakePitchGenerationRepository.jobState)
    }
}