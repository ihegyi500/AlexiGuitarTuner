package com.ihegyi.alexiguitartuner.feat_tuner.data

import com.ihegyi.alexiguitartuner.feat_tuner.data.fakes.FakePitchDetectionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PitchDetectionRepositoryTest {
    //SUT
    private val fakePitchDetectionRepository = FakePitchDetectionRepository()
    @Before
    fun setUp() {
        runTest {
            fakePitchDetectionRepository.initPitchList()
        }
    }

    @Test
    fun getPitchName_out_of_pitch_diff() {
        val expectedPitchName = ""
        var actualPitchName = fakePitchDetectionRepository.getPitchName(1.41)
        assertEquals(expectedPitchName, actualPitchName)
        actualPitchName = fakePitchDetectionRepository.getPitchName(1.59)
        assertEquals(expectedPitchName, actualPitchName)
    }

    @Test
    fun getPitchName_above_pitch_diff_below_fault_diff() {
        val expectedPitchName = "Below B1"
        var actualPitchName = fakePitchDetectionRepository.getPitchName(1.6)
        assertEquals(expectedPitchName, actualPitchName)
        actualPitchName = fakePitchDetectionRepository.getPitchName(1.79)
        assertEquals(expectedPitchName, actualPitchName)
    }

    @Test
    fun getPitchName_between_fault_diff() {
        val expectedPitchName = "B1"
        var actualPitchName = fakePitchDetectionRepository.getPitchName(1.8)
        assertEquals(expectedPitchName, actualPitchName)
        actualPitchName = fakePitchDetectionRepository.getPitchName(2.2)
        assertEquals(expectedPitchName, actualPitchName)
    }

    @Test
    fun getPitchName_above_fault_diff() {
        val expectedPitchName = "Above B1"
        var actualPitchName = fakePitchDetectionRepository.getPitchName(2.21)
        assertEquals(expectedPitchName, actualPitchName)
        actualPitchName = fakePitchDetectionRepository.getPitchName(2.4)
        assertEquals(expectedPitchName, actualPitchName)
    }
}