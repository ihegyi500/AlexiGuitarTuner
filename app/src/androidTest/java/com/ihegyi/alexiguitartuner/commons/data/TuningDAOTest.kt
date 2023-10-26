package com.ihegyi.alexiguitartuner.commons.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.commons.data.db.dao.TuningDAO
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import com.ihegyi.alexiguitartuner.commons.domain.entities.UserSettings
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

@SmallTest
@HiltAndroidTest
class TuningDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    //SUT
    private lateinit var tuningDAO: TuningDAO
    private lateinit var tunings : MutableList<Tuning>

    @Before
    fun setUp() {
        hiltRule.inject()
        tuningDAO = database.tuningDAO
        runTest {
            val tuningNames = mutableListOf<String>().apply {
                ('A'..'Z').forEach {
                    add(it.toString())
                }
            }
            tunings = mutableListOf<Tuning>().apply {
                tuningNames.forEachIndexed { index, name ->
                    add(Tuning(index.toLong() + 1, name, if(index < (tuningNames.size - 1) / 2) 1 else 2))
                    database.tuningDAO.insertTuning(last())
                }
            }
        }
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun getTuningById() = runTest {
        val actualTuning = tuningDAO.getTuningById(1)
        assertEquals(tunings[0], actualTuning)
    }
    @Test
    fun getTuningsByInstrument() = runTest {
        val expectedTunings = mutableListOf<Tuning>().apply {
            for (i in 0 until(tunings.size - 1) / 2) {
                add(tunings[i])
            }
        }
        val actualTuning = tuningDAO.getTuningsByInstrument(1).first()
        assertEquals(expectedTunings, actualTuning)
    }
    @Test
    fun getTuningNameBySettings() = runTest {
        database.userSettingsDAO.updateUserSettings(
            UserSettings(
                1,
                1,
                useSharp = false,
                useEnglish = false
            )
        )
        val expectedTuning = tunings[0].name
        val actualTuning = tuningDAO.getTuningNameBySettings()
        assertEquals(expectedTuning,actualTuning)
    }
    @Test
    fun deleteAllCustomTuningsAndGetLastTuningId() = runTest {
        tuningDAO.deleteAllCustomTunings()
        val expectedLastTuningId = 9.toLong()
        val actualLastTuningId = tuningDAO.getLastTuningId()
        assertEquals(expectedLastTuningId,actualLastTuningId)
    }
}