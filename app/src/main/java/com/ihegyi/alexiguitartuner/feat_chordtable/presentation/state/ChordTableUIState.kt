package com.ihegyi.alexiguitartuner.feat_chordtable.presentation.state

import com.ihegyi.alexiguitartuner.commons.domain.entities.Chord
import com.ihegyi.alexiguitartuner.commons.domain.entities.ChordTable
import com.ihegyi.alexiguitartuner.commons.domain.entities.Instrument
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning

data class ChordTableUIState(
    val instrumentList: List<Instrument>,
    val selectedInstrument: Instrument?,
    val tuningList: List<Tuning>,
    val selectedTuning: Tuning?,
    val chordList: List<Chord>,
    val selectedChord: Chord?,
    val chordTableList: List<ChordTable>,
    val selectedChordTable: ChordTable?,
    val selectedPosition : Int,
    val tuningPitches : List<Pitch>
) {
    companion object {
        val initial_state = ChordTableUIState(
            instrumentList = emptyList(),
            selectedInstrument = null,
            tuningList = emptyList(),
            selectedTuning = null,
            chordList = emptyList(),
            selectedChord = null,
            chordTableList = emptyList(),
            selectedChordTable = null,
            selectedPosition = 0,
            tuningPitches = emptyList()
        )
    }
}
