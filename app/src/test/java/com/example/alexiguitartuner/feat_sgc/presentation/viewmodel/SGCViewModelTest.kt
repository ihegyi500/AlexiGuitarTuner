package com.example.alexiguitartuner.feat_sgc.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.alexiguitartuner.commons.domain.entities.InstrumentString
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_sgc.data.fakes.FakeSGCRepository
import com.example.alexiguitartuner.feat_sgc.domain.usecase.CalculateStringGaugeUseCase
import com.example.alexiguitartuner.feat_tuner.data.fakes.FakeButtonGenerationRepository
import com.example.alexiguitartuner.feat_tuner.data.fakes.FakePitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.data.fakes.FakePitchGenerationRepository
import com.example.alexiguitartuner.feat_tuner.presentation.viewmodel.TunerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SGCViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //SUT
    private lateinit var sgcViewModel: SGCViewModel
    //FAKES
    private lateinit var fakeSGCRepository: FakeSGCRepository

    @Before
    fun setUp() {
        fakeSGCRepository = FakeSGCRepository()
        sgcViewModel = SGCViewModel(
            UnconfinedTestDispatcher(),
            CalculateStringGaugeUseCase(),
            fakeSGCRepository
        )
        ('A'..'Z').forEachIndexed { index, c ->
            fakeSGCRepository.pitches.add(
                Pitch(
                    index + 1.toDouble(),
                    c.toString() + index
                )
            )
            fakeSGCRepository.strings.add(
                InstrumentString(
                    index + 1,
                    index + 1.toDouble(),
                    FakeSGCRepository.DEFAULT_SCALE_LENGTH,
                    FakeSGCRepository.DEFAULT_TENSION
                )
            )
        }
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun getPitch() = runTest {
        val expectedPitchName = "D3"
        val actualPitchName = sgcViewModel.getPitch(4.toDouble())?.name
        assertEquals(expectedPitchName,actualPitchName)
    }

    @Test
    fun getPitchByName() = runTest {
        val expectedPitchFreq = 5.0
        val actualPitchFreq = sgcViewModel.getPitchByName("E4")?.frequency
        assertEquals(expectedPitchFreq,actualPitchFreq)
    }

    @Test
    fun insertStringToFilledList() = runTest {
        sgcViewModel.insertString()
        val expectedLastString = InstrumentString(
            27,
            440.0,
            25.5,
            18.0
        )
        val actualLastString = fakeSGCRepository.strings.last()
        assertEquals(expectedLastString,actualLastString)
    }
    @Test
    fun insertStringToEmptyList() = runTest {
        fakeSGCRepository.strings.clear()
        sgcViewModel.insertString()
        val expectedLastString = InstrumentString(
            1,
            440.0,
            25.5,
            18.0
        )
        val actualLastString = fakeSGCRepository.strings.last()
        assertEquals(expectedLastString,actualLastString)
    }

    @Test
    fun updateString() {
        val expectedString = InstrumentString(
            fakeSGCRepository.strings.size,
            FakeSGCRepository.CONCERT_PITCH + 1,
            FakeSGCRepository.DEFAULT_SCALE_LENGTH + 1,
            FakeSGCRepository.DEFAULT_TENSION + 1
        )
        sgcViewModel.updateString(expectedString)
        val actualString = fakeSGCRepository.strings[fakeSGCRepository.strings.size - 1]
        assertEquals(expectedString,actualString)
    }

    @Test
    fun deleteString() = runTest {
        val stringToBeDeleted = fakeSGCRepository.strings[0]
        sgcViewModel.deleteString(stringToBeDeleted)
        val expectedString = InstrumentString (
            1,
            2.0,
            FakeSGCRepository.DEFAULT_SCALE_LENGTH,
            FakeSGCRepository.DEFAULT_TENSION
        )
        val actualString = fakeSGCRepository.strings[0]
        assertEquals(expectedString,actualString)
    }

    @Test
    fun calculateStringGauge() = runTest{
        var actualStringGauge = sgcViewModel.calculateStringGauge(fakeSGCRepository.strings.first())
        assertEquals("3,4736 in",actualStringGauge)
        actualStringGauge = sgcViewModel.calculateStringGauge(fakeSGCRepository.strings.last())
        assertEquals("0,1336 in",actualStringGauge)
    }
}