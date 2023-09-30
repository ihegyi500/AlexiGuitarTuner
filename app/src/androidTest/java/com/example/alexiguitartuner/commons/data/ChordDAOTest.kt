package com.example.alexiguitartuner.commons.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.test.filters.SmallTest
import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.data.db.dao.ChordDAO
import com.example.alexiguitartuner.commons.data.db.dao.PitchDAO
import com.example.alexiguitartuner.commons.data.db.dao.StringDAO
import com.example.alexiguitartuner.commons.domain.entities.Chord
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
class ChordDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    //SUT
    private lateinit var chordDAO: ChordDAO
    private lateinit var chords: MutableList<Chord>
    @Before
    fun setUp() {
        hiltRule.inject()
        chordDAO = database.chordDAO
        chords = mutableListOf()
        runTest {
            ('A'..'G').forEachIndexed { index, name ->
                chords.add(Chord(
                    index.toLong() + 1,
                    name.toString(),
                    1.toLong()
                ))
                chordDAO.insertChord(chords.last())
            }
        }
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun getChordById() = runTest {
        val actualChord = chordDAO.getChordById(1)
        assertEquals(chords[0],actualChord)
    }

    @Test
    fun getChordsByTuning()= runTest {
        val actualChord = chordDAO.getChordsByTuning(1).first()
        assertEquals(chords,actualChord)
    }
}