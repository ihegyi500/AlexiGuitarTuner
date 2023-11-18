package com.ihegyi.alexiguitartuner.feat_chordtable.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihegyi.alexiguitartuner.commons.domain.entities.Chord
import com.ihegyi.alexiguitartuner.commons.domain.entities.ChordTable
import com.ihegyi.alexiguitartuner.commons.domain.entities.Instrument
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import com.ihegyi.alexiguitartuner.feat_chordtable.domain.ChordTableRepository
import com.ihegyi.alexiguitartuner.feat_chordtable.presentation.state.ChordTableUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChordTableViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val chordTableRepository: ChordTableRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChordTableUIState.initial_state)
    val uiState: StateFlow<ChordTableUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            chordTableRepository.getInstruments().collectLatest { instruments ->
                val selectedInstrument = instruments.firstOrNull()
                _uiState.update {
                    _uiState.value.copy(
                        instrumentList = instruments,
                        selectedInstrument = selectedInstrument
                    )
                }
                selectInstrument(selectedInstrument)
            }
        }
    }

    suspend fun selectInstrument(instrument: Instrument?) {
        val tuningList = chordTableRepository.getTuningsByInstrument(instrument?.instrumentId ?: 0).firstOrNull() ?: emptyList()
        val selectedTuning = tuningList.firstOrNull()
        _uiState.update {
            _uiState.value.copy(
                selectedInstrument = instrument,
                tuningList = tuningList
            )
        }
        selectTuning(selectedTuning)
    }

    suspend fun selectTuning(tuning: Tuning?) {
        val chordList = chordTableRepository.getChordsByTuning(tuning?.tuningId ?: 0).firstOrNull() ?: emptyList()
        val selectedChord = chordList.firstOrNull()
        val tuningPitches = chordTableRepository.getPitchesByTuning(tuning?.tuningId ?: 0).firstOrNull() ?: emptyList()
        _uiState.update {
            _uiState.value.copy(
                selectedTuning = tuning,
                chordList = chordList,
                tuningPitches = tuningPitches
            )
        }
        selectChord(selectedChord)
    }

    suspend fun selectChord(chord: Chord?) {
        val chordTableList = chordTableRepository.getChordTablesByChord(chord?.chordId ?: 0).firstOrNull() ?: emptyList()
        val selectedChordTable = chordTableList.firstOrNull()
        _uiState.update {
            _uiState.value.copy(
                selectedChord = chord,
                chordTableList = chordTableList
            )
        }
        selectChordTable(selectedChordTable)
    }

    private fun selectChordTable(chordTable: ChordTable?) {
        val selectedPosition = chordTable?.position ?: 0
        _uiState.update {
            _uiState.value.copy(
                selectedChordTable = chordTable,
                selectedPosition = selectedPosition
            )
        }
    }

    fun increaseChordTablePosition() {
        val newPosition = _uiState.value.selectedPosition + 1
        if( newPosition < _uiState.value.chordTableList.size) {
            selectChordTable(_uiState.value.chordTableList[newPosition])
        }
    }

    fun decreaseChordTablePosition() {
        val newPosition = _uiState.value.selectedPosition - 1
        if( newPosition >= 0) {
            selectChordTable(_uiState.value.chordTableList[newPosition])
        }
    }
}
