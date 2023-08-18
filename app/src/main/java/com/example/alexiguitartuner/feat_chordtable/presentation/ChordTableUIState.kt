package com.example.alexiguitartuner.feat_chordtable.presentation

import com.example.alexiguitartuner.commons.domain.Chord
import com.example.alexiguitartuner.commons.domain.ChordTable
import com.example.alexiguitartuner.commons.domain.Instrument
import com.example.alexiguitartuner.commons.domain.Pitch
import com.example.alexiguitartuner.commons.domain.Tuning

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
)
