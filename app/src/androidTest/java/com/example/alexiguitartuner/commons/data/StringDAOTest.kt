package com.example.alexiguitartuner.commons.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.test.filters.SmallTest
import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.data.db.dao.PitchDAO
import com.example.alexiguitartuner.commons.data.db.dao.StringDAO
import com.example.alexiguitartuner.commons.domain.entities.InstrumentString
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
class StringDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    //SUT
    private lateinit var stringDAO: StringDAO
    private lateinit var strings: MutableList<InstrumentString>
    @Before
    fun setUp() {
        hiltRule.inject()
        stringDAO = database.stringDAO
        runTest {
            strings = mutableListOf<InstrumentString>().apply {
                for (i in 1..6) {
                    val string = InstrumentString(
                        i,
                        440.0,
                        25.5,
                        18.0
                    )
                    add(string)
                    stringDAO.insertString(string)
                }
            }
        }
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun getInstrumentStrings() = runTest {
        val actualStrings = stringDAO.getInstrumentStrings().first()
        assertEquals(strings,actualStrings)
    }
    @Test
    fun updateString() = runTest {
        val expectedString = InstrumentString(
            3,
            330.0,
            21.5,
            19.0
        )
        stringDAO.updateString(expectedString)
        val actualString = stringDAO.getInstrumentStrings().first()[2]
        assertEquals(expectedString,actualString)
    }
    @Test
    fun deleteStringAndDecrementStringNumber() = runTest {
        val expectedString = InstrumentString(
            2,
            440.0,
            25.5,
            18.0
        )
        stringDAO.deleteString(expectedString)
        var actualString = stringDAO.getInstrumentStrings().first()[1]
        assertNotEquals(expectedString,actualString)
        stringDAO.decrementRemainingStringNumbers(2)
        actualString = stringDAO.getInstrumentStrings().first()[1]
        assertEquals(expectedString,actualString)
    }
    @Test
    fun getLastElement() = runTest {
        val actualString = stringDAO.getLastElement()
        assertEquals(strings.last(),actualString)
    }
    @Test
    fun getCountOfInstrumentStrings() = runTest {
        val expectedNum = 6
        val actualNum = stringDAO.getCountOfInstrumentStrings()
        assertEquals(expectedNum,actualNum)
    }
}