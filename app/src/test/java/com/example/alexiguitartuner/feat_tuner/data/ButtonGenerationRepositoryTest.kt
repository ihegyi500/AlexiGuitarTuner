package com.example.alexiguitartuner.feat_tuner.data

import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.feat_tuner.data.fakes.FakeButtonGenerationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ButtonGenerationRepositoryTest {
    private val fakeButtonGenerationRepository = FakeButtonGenerationRepository()
    @Before
    fun setUp() {
        fakeButtonGenerationRepository.pitches = emptyList<Pitch>().toMutableList()
        fakeButtonGenerationRepository.pitchTuningCrossRef = emptyList<PitchTuningCrossRef>().toMutableList()
        fakeButtonGenerationRepository.tunings = emptyList<Tuning>().toMutableList()

        ('A'..'H').forEachIndexed { index, c ->
            fakeButtonGenerationRepository.pitches.add(
                Pitch(index.toDouble(), c.toString() + index)
            )
            if (index < 4) {
                fakeButtonGenerationRepository.pitchTuningCrossRef.add(
                    PitchTuningCrossRef(
                        fakeButtonGenerationRepository.userSettings.tuningId,index.toDouble()
                    )
                )
            }
            fakeButtonGenerationRepository.tunings.add(
                index,
                Tuning(
                    index.toLong() + 1,
                    "Tuning $c",
                    index.toLong()
                )
            )
        }
    }

    @Test
    fun getPitchesOfLastTuning() {
        runBlocking {
            val expectedPitchesOfLastTuning = mutableListOf<Pitch>().apply {
                ('A'..'D').forEachIndexed { index, c ->
                    add(
                        Pitch(index.toDouble(), c.toString() + index)
                    )
                }
            }
            val actualPitchesOfLastTuning = fakeButtonGenerationRepository.getPitchesOfLastTuning()
            assertEquals(expectedPitchesOfLastTuning,actualPitchesOfLastTuning)
        }
    }

    @Test
    fun getTuningNameBySettings() {
        runBlocking {
            val expectedTuningName = "Tuning A"
            val actualTuningName = fakeButtonGenerationRepository.getTuningNameBySettings()
            assertEquals(expectedTuningName, actualTuningName)
        }
    }
}