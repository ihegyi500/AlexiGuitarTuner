package com.example.alexiguitartuner.commons.domain

import com.example.alexiguitartuner.commons.domain.entities.Instrument
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import kotlinx.coroutines.flow.StateFlow

interface TuningSelectorViewModel {

    val uiState : StateFlow<Any>
    suspend fun selectInstrument(instrument: Instrument?)
    suspend fun selectTuning(tuning: Tuning?)

}