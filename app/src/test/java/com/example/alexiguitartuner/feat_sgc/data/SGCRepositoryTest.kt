package com.example.alexiguitartuner.feat_sgc.data

import com.example.alexiguitartuner.commons.domain.entities.InstrumentString
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_sgc.data.fakes.FakeSGCRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class SGCRepositoryTest {
    private val fakeSGCRepository = FakeSGCRepository()
    @Before
    fun setUp() {
        fakeSGCRepository.strings = emptyList<InstrumentString>().toMutableList()
        fakeSGCRepository.pitches = emptyList<Pitch>().toMutableList()

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
    }

    @Test
    fun insertString_filled_list() {
        runBlocking {
            fakeSGCRepository.getInstrumentStrings().collectLatest { strings ->
                fakeSGCRepository.insertString()
                assertEquals(
                    InstrumentString(
                        strings.size,
                        FakeSGCRepository.CONCERT_PITCH,
                        FakeSGCRepository.DEFAULT_SCALE_LENGTH,
                        FakeSGCRepository.DEFAULT_TENSION
                    ),
                    strings[strings.size - 1]
                )
            }
        }
    }

    @Test
    fun insertString_empty_list() {
        runBlocking {
            fakeSGCRepository.getInstrumentStrings().collectLatest { strings ->
                fakeSGCRepository.strings.clear()
                fakeSGCRepository.insertString()
                assertEquals(
                    InstrumentString(
                        strings.size,
                        FakeSGCRepository.CONCERT_PITCH,
                        FakeSGCRepository.DEFAULT_SCALE_LENGTH,
                        FakeSGCRepository.DEFAULT_TENSION
                    ),
                    strings[strings.size - 1]
                )
            }
        }
    }

    @Test
    fun getPitch() {
        runBlocking {
            val expectedPitchName = "D3"
            val actualPitchName = fakeSGCRepository.getPitch(4.toDouble())?.name
            assertEquals(expectedPitchName,actualPitchName)
        }
    }

    @Test
    fun getPitchByName() {
        runBlocking {
            val expectedPitchFreq = 5.0
            val actualPitchFreq = fakeSGCRepository.getPitchByName("E4")?.frequency
            assertEquals(expectedPitchFreq,actualPitchFreq)
        }
    }

    @Test
    fun updateString() {
        runBlocking {
            val expectedString = InstrumentString(
                fakeSGCRepository.strings.size,
                FakeSGCRepository.CONCERT_PITCH + 1,
                FakeSGCRepository.DEFAULT_SCALE_LENGTH + 1,
                FakeSGCRepository.DEFAULT_TENSION + 1
            )
            fakeSGCRepository.updateString(expectedString)
            val actualString = fakeSGCRepository.strings[fakeSGCRepository.strings.size - 1]
            assertEquals(expectedString,actualString)
        }
    }

    @Test
    fun deleteString() {
        runBlocking {
            val stringToBeDeleted = fakeSGCRepository.strings[0]
            fakeSGCRepository.deleteString(stringToBeDeleted)
            val expectedString = InstrumentString (
                1,
                1.0,
                FakeSGCRepository.DEFAULT_SCALE_LENGTH,
                FakeSGCRepository.DEFAULT_TENSION
            )
            val actualString = fakeSGCRepository.strings[0]
            assertEquals(expectedString,actualString)
        }
    }
}