package com.example.alexiguitartuner.feat_tuner.presentation.state

import com.example.alexiguitartuner.commons.domain.entities.Pitch

data class PitchButtonsUIState(
    val tuningName : String,
    val pitchList : List<Pitch>
) {
    companion object {
        val initial_state = PitchButtonsUIState(
            tuningName = "",
            pitchList = emptyList()
        )
    }
}
