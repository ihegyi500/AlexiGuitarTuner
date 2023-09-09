package com.example.alexiguitartuner.commons.presentation.state

import com.example.alexiguitartuner.commons.domain.entities.Instrument
import com.example.alexiguitartuner.commons.domain.entities.Pitch

data class CreateTuningUIState(
    val instrumentList: List<Instrument>,
    val selectedInstrument: Instrument?,
    var pitchesOfTuning : MutableList<Pitch>
){
    companion object {
        val concert_pitch = Pitch(440.0,"A4")
        val initial_state = CreateTuningUIState(
            instrumentList = emptyList(),
            selectedInstrument = null,
            pitchesOfTuning = MutableList(6) { concert_pitch }
        )
    }
}
