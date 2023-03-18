package com.example.alexiguitartuner.feat_chordtable.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.feat_chordtable.data.ChordTableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChordTableViewModel @Inject constructor(
    private val chordTableRepository: ChordTableRepository
) : ViewModel() {



    suspend fun getInstrumentsWithTuningsAndChords() =
        chordTableRepository.getInstrumentsWithTuningsAndChords()

    suspend fun getInstrumentNames() =
        chordTableRepository.getInstrumentNames()

    suspend fun getTuningsByInstrument(instrumentName : String) : List<String> =
        chordTableRepository.getTuningsByInstrument(instrumentName)

    suspend fun getChordsByTuning(instrumentName : String, tuningName : String) : List<String> =
        chordTableRepository.getChordsByTuning(instrumentName,tuningName)

}