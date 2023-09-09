package com.example.alexiguitartuner.commons.presentation.state

import com.example.alexiguitartuner.commons.domain.entities.Instrument
import com.example.alexiguitartuner.commons.domain.entities.Tuning

data class SelectTuningUIState (
    val instrumentList: List<Instrument>,
    val selectedInstrument: Instrument?,
    val tuningList: List<Tuning>,
    val selectedTuning: Tuning?
) {
    companion object {
        val initial_state = SelectTuningUIState(
            instrumentList = emptyList(),
            selectedInstrument = null,
            tuningList = emptyList(),
            selectedTuning = null
        )
    }
}