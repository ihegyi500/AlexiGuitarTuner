package com.ihegyi.alexiguitartuner.feat_chordtable.data.fakes

import com.ihegyi.alexiguitartuner.commons.domain.entities.Chord
import com.ihegyi.alexiguitartuner.commons.domain.entities.ChordTable
import com.ihegyi.alexiguitartuner.commons.domain.entities.Instrument
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import com.ihegyi.alexiguitartuner.feat_chordtable.domain.ChordTableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeChordTableRepository : ChordTableRepository {
    private val listOfInstruments = mutableListOf<Instrument>()
    private val listOfTunings = mutableListOf<Tuning>()
    private val listOfChords = mutableListOf<Chord>()
    private val listOfChordTables = mutableListOf<ChordTable>()
    private val listOfPitches = mutableListOf<Pitch>()

    init {
        ('a'..'z').forEachIndexed { index, c ->
            listOfInstruments.add(Instrument(index + 1L,"Instrument $c",index + 1))
            listOfTunings.add(Tuning(index + 1L,"Tuning $c",index + 1L))
            listOfChords.add(Chord(index + 1L,"Chord $c",index + 1L))
            listOfChordTables.add(ChordTable(index + 1L,index + 1L,index, listOf(0,1,2,3)))
            listOfPitches.add(Pitch(index.toDouble(),"Pitch $c"))
        }
    }
    override fun getInstruments(): Flow<List<Instrument>> = flow { emit(listOfInstruments) }
    override fun getTuningsByInstrument(id: Long?): Flow<List<Tuning>> = flow { emit(listOfTunings) }
    override fun getChordsByTuning(tuningId: Long?): Flow<List<Chord>> = flow { emit(listOfChords) }
    override fun getChordTablesByChord(chordId: Long?): Flow<List<ChordTable>> = flow { emit(listOfChordTables) }
    override fun getPitchesByTuning(tuningId: Long): Flow<List<Pitch>> = flow { emit(listOfPitches) }


}