package com.example.alexiguitartuner.feat_chordtable.presentation

data class ChordTableUIState(
    val listOfInstruments : List<String> = emptyList(),
    val listOfTunings : List<String> = emptyList(),
    val listOfChords : List<String> = emptyList(),
    val selectedInstrument : String = "",
    val selectedTuning : String = "",
    val selectedChord : String = ""
)
