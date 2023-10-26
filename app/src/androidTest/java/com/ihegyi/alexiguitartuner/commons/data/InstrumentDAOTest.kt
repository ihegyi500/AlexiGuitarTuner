package com.ihegyi.alexiguitartuner.commons.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.commons.data.db.dao.InstrumentDAO
import com.ihegyi.alexiguitartuner.commons.domain.entities.Instrument
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
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
class InstrumentDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    //SUT
    private lateinit var instrumentDAO: InstrumentDAO
    private lateinit var instruments: MutableList<Instrument>
    @Before
    fun setUp() {
        hiltRule.inject()
        instrumentDAO = database.instrumentDAO
        runTest {
            val instrumentNames = listOf("Guitar", "Piano", "Drums")
            instruments = mutableListOf<Instrument>().apply {
                instrumentNames.forEachIndexed { index, name ->
                    add(Instrument(index.toLong() + 1, name, index + 1))
                }
            }
            database.instrumentDAO.insertInstruments(instruments)
        }
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun getInstruments() = runTest{
        val actualInstrumentList = instrumentDAO.getInstruments().first()
        assertEquals(instruments, actualInstrumentList)
    }
    @Test
    fun getInstrumentById() = runTest {
        val actualInstrument = instrumentDAO.getInstrumentById(1)
        assertEquals(instruments[0],actualInstrument)
    }
    @Test
    fun getInstrumentByTuningId() = runTest {
        val tuningNames = listOf("Standard E", "Standard D", "Drop C")
        val tuningList = mutableListOf<Tuning>().apply {
            tuningNames.forEachIndexed { index, name ->
                add(Tuning(index.toLong() + 1, name, 1))
                database.tuningDAO.insertTuning(last())
            }
        }
        val expectedInstrument = instruments[0]
        val actualInstrument = instrumentDAO.getInstrumentByTuningId(tuningList[0].tuningId)
        assertEquals(expectedInstrument,actualInstrument)
    }
}