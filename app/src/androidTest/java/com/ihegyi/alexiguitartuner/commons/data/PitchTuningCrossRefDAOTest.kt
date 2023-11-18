package com.ihegyi.alexiguitartuner.commons.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.commons.data.db.dao.PitchTuningCrossRefDAO
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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

@SmallTest
@HiltAndroidTest
class PitchTuningCrossRefDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    //SUT
    private lateinit var pitchTuningCRDAO: PitchTuningCrossRefDAO
    private lateinit var tunings : MutableList<Tuning>
    private lateinit var pitches : MutableList<Pitch>

    @Before
    fun setUp() {
        hiltRule.inject()
        pitchTuningCRDAO = database.pitchTuningCrossRefDAO
        runTest {
            val tuningNames = mutableListOf<String>().apply {
                ('A'..'Z').forEach {
                    add(it.toString())
                }
            }
            tunings = mutableListOf<Tuning>().apply {
                tuningNames.forEachIndexed { index, name ->
                    add(
                        Tuning(
                            index.toLong() + 1,
                            name,
                            if (index < (tuningNames.size - 1) / 2) 1 else 2
                        )
                    )
                    database.tuningDAO.insertTuning(last())
                }
            }
            val semitoneFactor = 2.0.pow(1.0 / 12.0)
            var frequency = 16.351597831287375
            val names = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
            pitches = mutableListOf<Pitch>().apply {
                for (octave in 0 until 2) {
                    for (name in names) {
                        add(Pitch(frequency, "$name$octave"))
                        frequency *= semitoneFactor
                    }
                }
            }
            database.pitchDAO.updatePitches(pitches)
            for (crossRefIndex in 1..10) {
                pitchTuningCRDAO.insertPitchTuningCrossRef(
                    PitchTuningCrossRef(
                        tunings[crossRefIndex].tuningId,
                        pitches[crossRefIndex].frequency
                    )
                )
            }
        }
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun deleteAllCustomTunings() = runTest {
        pitchTuningCRDAO.deleteAllCustomTunings()
        val pitchesOfNotCustomTuning = database.pitchDAO.getPitchesByTuning(9).first()
        val pitchesOfCustomTuning = database.pitchDAO.getPitchesByTuning(10).first()
        assertNotEquals(pitchesOfNotCustomTuning, emptyList<Pitch>())
        assertEquals(pitchesOfCustomTuning, emptyList<Pitch>())
    }
}