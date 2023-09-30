package com.example.alexiguitartuner.commons.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.data.db.dao.PitchDAO
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.pow

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class PitchDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    //SUT
    private lateinit var pitchDAO: PitchDAO
    private lateinit var pitches: MutableList<Pitch>
    @Before
    fun setUp() {
        hiltRule.inject()
        pitchDAO = database.pitchDAO
        runTest {
            val semitoneFactor = 2.0.pow(1.0 / 12.0)
            var frequency = 16.351597831287375
            val names = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
            pitches = mutableListOf<Pitch>().apply {
                for (octave in 0 until 9) {
                    for (name in names) {
                        add(Pitch(frequency, "$name$octave"))
                        frequency *= semitoneFactor
                    }
                }
            }
            pitchDAO.updatePitches(pitches)
        }
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun getPitches() = runTest {
        val actualPitches = pitchDAO.getPitches()
        assertEquals(pitches, actualPitches)
    }
    @Test
    fun getPitch() = runTest {
        val expectedPitch = pitches[0].name
        val actualPitch = pitchDAO.getPitch(pitches[0].frequency)?.name
        assertEquals(expectedPitch,actualPitch)
    }
    @Test
    fun getPitchByName() = runTest {
        val expectedPitch = pitches[0].frequency
        val actualPitch = pitchDAO.getPitchByName(pitches[0].name)?.frequency
        assertEquals(expectedPitch,actualPitch)
    }
    @Test
    fun getPitchesByTuning() = runTest {
        val tuningNames = listOf("Standard E", "Standard D", "Drop C")
        val tuningList = mutableListOf<Tuning>().apply {
            tuningNames.forEachIndexed { index, name ->
                add(Tuning(index.toLong() + 1, name, 0))
                database.tuningDAO.insertTuning(last())
            }
        }
        val expectedPitches = mutableListOf<Pitch>().apply {
            for (i in 0 until 10) {
                add(pitches[i])
                database.pitchTuningCrossRefDAO.insertPitchTuningCrossRef(
                    PitchTuningCrossRef(
                        tuningList[0].tuningId,
                        pitches[i].frequency
                    )
                )
            }
        }
        val actualPitches = pitchDAO.getPitchesByTuning(tuningList[0].tuningId).first()
        assertEquals(expectedPitches,actualPitches)
    }
    @Test
    fun getPitchesByLastTuning() = runTest {
        val tuningNames = listOf("Standard E", "Standard D", "Drop C")
        val tuningList = mutableListOf<Tuning>().apply {
            tuningNames.forEachIndexed { index, name ->
                add(Tuning(index.toLong() + 1, name, 0))
                database.tuningDAO.insertTuning(last())
            }
        }
        val expectedPitches = mutableListOf<Pitch>().apply {
            for (i in pitches.size - 1 downTo pitches.size - 10) {
                add(pitches[i])
                database.pitchTuningCrossRefDAO.insertPitchTuningCrossRef(
                    PitchTuningCrossRef(
                        tuningList[0].tuningId,
                        pitches[i].frequency
                    )
                )
            }
        }
        database.userSettingsDAO.updateUserSettings(
            UserSettings(
                1,
                tuningList[0].tuningId,
                useSharp = false,
                useEnglish = false
            )
        )
        val actualPitches = pitchDAO.getPitchesByLastTuning()
        assertEquals(expectedPitches,actualPitches)
    }
}