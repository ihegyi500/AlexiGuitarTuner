package com.ihegyi.alexiguitartuner.feat_tuner.presentation.state

import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch

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
