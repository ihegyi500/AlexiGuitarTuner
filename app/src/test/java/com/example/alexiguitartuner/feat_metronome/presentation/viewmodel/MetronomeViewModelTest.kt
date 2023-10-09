package com.example.alexiguitartuner.feat_metronome.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.alexiguitartuner.feat_metronome.data.fakes.FakeMetronomeRepository
import com.example.alexiguitartuner.feat_metronome.domain.Beat
import com.example.alexiguitartuner.feat_metronome.domain.MetronomeState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MetronomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //SUT
    private lateinit var metronomeViewModel: MetronomeViewModel
    //FAKES
    private lateinit var fakeMetronomeRepository: FakeMetronomeRepository

    @Before
    fun setUp() {
        fakeMetronomeRepository = FakeMetronomeRepository()
        metronomeViewModel = MetronomeViewModel(
            fakeMetronomeRepository
        )
    }

    @Test
    fun startAndStopService() = runTest {
        fakeMetronomeRepository.playMetronome()
        var actualIsPlaying = metronomeViewModel.getMetronomeState().first().isPlaying
        assertEquals(true,actualIsPlaying)
        fakeMetronomeRepository.pauseMetronome()
        actualIsPlaying = metronomeViewModel.getMetronomeState().first().isPlaying
        assertEquals(false,actualIsPlaying)
    }

    @Test
    fun getMetronomeState() = runTest {
        val expectedMetronomeState = MetronomeState.initial_state
        val actualMetronomeState = metronomeViewModel.getMetronomeState().first()
        assertEquals(expectedMetronomeState,actualMetronomeState)
    }
    @Test
    fun insertMaxNote() = runTest {
        try {
                for (i in 0..FakeMetronomeRepository.MAX_NOTE) {
                    metronomeViewModel.insertNote()
                }
            } catch (e: Exception) {
                assertEquals(
                    "Only ${FakeMetronomeRepository.MAX_NOTE} notes are allowed!",
                    e.message
                )
            } finally {
                fakeMetronomeRepository.resetMetronomeState()
            }
    }

    @Test
    fun removeMinNote() = runTest {
        try {
            for (i in FakeMetronomeRepository.MAX_NOTE downTo FakeMetronomeRepository.MIN_NOTE) {
                metronomeViewModel.removeNote()
            }
        } catch (e : Exception) {
            assertEquals("At least ${FakeMetronomeRepository.MIN_NOTE} notes are necessary!",e.message)
        }
    }

    @Test
    fun setBPM() = runTest {
        val expectedBPM = 120
        metronomeViewModel.setBPM(expectedBPM)
        val actualBPM = metronomeViewModel.getMetronomeState().first().bpm
        assertEquals(expectedBPM,actualBPM)
        val expectedTempo = 500L
        val actualTempo = metronomeViewModel.getMetronomeState().first().tempo
        assertEquals(expectedTempo,actualTempo)
    }

    @Test
    fun setRhythm() = runTest {
        val previousRhythm = metronomeViewModel.getMetronomeState().first().rhythm
        metronomeViewModel.setRhythm()
        val actualRhythm = metronomeViewModel.getMetronomeState().first().rhythm
        assertEquals(previousRhythm.getNextRhythm(),actualRhythm)
    }

    @Test
    fun setToneByIndex() = runTest {
        metronomeViewModel.setToneByIndex(0)
        val expectedBeat = Beat.LOUDER
        val actualBeat = metronomeViewModel.getMetronomeState().first().beatList.first()
        assertEquals(expectedBeat, actualBeat)
    }
}