package com.ihegyi.alexiguitartuner.commons.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ihegyi.alexiguitartuner.commons.data.db.AppDatabase
import com.ihegyi.alexiguitartuner.commons.data.db.dao.ChordTableDAO
import com.ihegyi.alexiguitartuner.commons.domain.entities.ChordTable
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

@SmallTest
@HiltAndroidTest
class ChordTableDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    //SUT
    private lateinit var chordTableDAO: ChordTableDAO
    private lateinit var chordTables: MutableList<ChordTable>
    @Before
    fun setUp() {
        hiltRule.inject()
        chordTableDAO = database.chordTableDAO
        chordTables = mutableListOf()
        runTest {
            for (i in 1..10) {
                chordTables.add(
                    ChordTable(
                        i.toLong(),
                        if (i < 5 ) 1 else 2,
                        i,
                        emptyList()
                    )
                )
                chordTableDAO.insertChordTable(chordTables.last())
            }
        }
    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun getPositionOfChordTable() = runTest {
        val expectedPosition = 4
        val actualPosition = chordTableDAO.getPositionOfChordTable(4)
        assertEquals(expectedPosition,actualPosition)
    }

    @Test
    fun getChordTablesByChord()= runTest {
        val expectedChordTables = chordTables.filter { it.chordId == 1.toLong() }
        val actualChordTables = chordTableDAO.getChordTablesByChord(1).first()
        assertEquals(expectedChordTables,actualChordTables)
    }
}