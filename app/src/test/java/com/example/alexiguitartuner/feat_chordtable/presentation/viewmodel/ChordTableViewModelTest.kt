package com.example.alexiguitartuner.feat_chordtable.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.alexiguitartuner.commons.domain.entities.Chord
import com.example.alexiguitartuner.commons.domain.entities.ChordTable
import com.example.alexiguitartuner.commons.domain.entities.Instrument
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.feat_chordtable.data.fakes.FakeChordTableRepository
import com.example.alexiguitartuner.feat_sgc.data.fakes.FakeSGCRepository
import com.example.alexiguitartuner.feat_sgc.presentation.viewmodel.SGCViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChordTableViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //SUT
    private lateinit var chordTableViewModel: ChordTableViewModel
    //FAKES
    private lateinit var fakeChordTableRepository: FakeChordTableRepository

    @Before
    fun setUp() {
        fakeChordTableRepository = FakeChordTableRepository()
        chordTableViewModel = ChordTableViewModel(
            UnconfinedTestDispatcher(),
            fakeChordTableRepository
        )

    }

    @Test
    fun selectInstrument() = runTest {
        val expectedInstrument = Instrument(26L,"Instrument z",26)
        val actualInstrument = fakeChordTableRepository.getInstruments().first().last()
        assertEquals(expectedInstrument,actualInstrument)
    }

    @Test
    fun selectTuning() = runTest {
        val expectedTuning = Tuning(1L,"Tuning a",1)
        val actualTuning = fakeChordTableRepository.getTuningsByInstrument(1).first().first()
        assertEquals(expectedTuning,actualTuning)
    }

    @Test
    fun selectChord() = runTest {
        val expectedChord = Chord(26L,"Chord z",26L)
        val actualChord = fakeChordTableRepository.getChordsByTuning(26L).first().last()
        assertEquals(expectedChord,actualChord)
    }

    @Test
    fun getChordTablePosition() = runTest {
        val expectedPosition = 0
        val actualPosition = fakeChordTableRepository.getChordTablesByChord(2).first()[0].position
        assertEquals(expectedPosition,actualPosition)
    }
}