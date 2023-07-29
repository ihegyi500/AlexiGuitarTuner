package com.example.alexiguitartuner.feat_chordtable.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.feat_chordtable.data.ChordTableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChordTableViewModel @Inject constructor(
    private val chordTableRepository: ChordTableRepository
) : ViewModel() {

    private var _chordTableUIState = MutableStateFlow(ChordTableUIState())
    val chordTableUIState : StateFlow<ChordTableUIState> = _chordTableUIState.asStateFlow()
/*
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedInstrumentList = chordTableRepository.getInstrumentNames()
            val updatedTuningList = chordTableRepository.getTuningsByInstrument(updatedInstrumentList.first())
            val updatedChordList = chordTableRepository.getChordsByTuning(
                updatedInstrumentList.first(),
                updatedTuningList.first()
            )

            _chordTableUIState.update {
                it.copy(listOfInstruments = updatedInstrumentList,
                    listOfTunings = updatedTuningList,
                    listOfChords = updatedChordList,
                    selectedInstrument = updatedInstrumentList.first(),
                    selectedTuning = if(updatedTuningList.isNotEmpty()) updatedTuningList.first() else "",
                    selectedChord = if(updatedChordList.isNotEmpty()) updatedChordList.first() else "")
            }
        }
    }

    fun updateTuningListByInstrument(instrumentName : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedTuningList = chordTableRepository.getTuningsByInstrument(instrumentName)
            val updatedChordList = chordTableRepository.getChordsByTuning(
                instrumentName,
                if(updatedTuningList.isNotEmpty()) updatedTuningList.first() else ""
            )
            _chordTableUIState.update {
                it.copy(
                    listOfTunings = updatedTuningList,
                    listOfChords = updatedChordList,
                    selectedInstrument = instrumentName,
                    selectedTuning = if(updatedTuningList.isNotEmpty()) updatedTuningList.first() else "",
                    selectedChord = if(updatedChordList.isNotEmpty()) updatedChordList.first() else ""
                )
            }
        }
    }
    fun getChordsByTuning(instrumentName : String, tuningName : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedChordList = chordTableRepository.getChordsByTuning(
                instrumentName,
                tuningName
            )
            _chordTableUIState.update {
                it.copy(
                    listOfChords = updatedChordList,
                    selectedTuning = tuningName,
                    selectedChord = if(updatedChordList.isNotEmpty()) updatedChordList.first() else ""
                )
            }
        }
    }

*/

}