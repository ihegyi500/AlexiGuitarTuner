package com.example.alexiguitartuner.commons.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.commons.data.UserSettingsRepository
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.commons.presentation.state.CreateTuningUIState
import com.example.alexiguitartuner.commons.presentation.state.CreateTuningUIState.Companion.concert_pitch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTuningViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(CreateTuningUIState.initial_state)
    val uiState : StateFlow<CreateTuningUIState> = _uiState.asStateFlow()

    private lateinit var _pitchList :  List<Pitch>
    val pitchList get() = _pitchList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _pitchList = userSettingsRepository.getPitches()
            userSettingsRepository.getInstruments().collectLatest {
                _uiState.value = _uiState.value.copy(
                    instrumentList = it,
                    selectedInstrument = it.first()
                )
            }
        }
    }

    fun changeSelectedInstrument(pos: Int) {
        val selectedInstrument = _uiState.value.instrumentList[pos]
        _uiState.value = _uiState.value.copy(
            selectedInstrument = selectedInstrument
        )
        if (selectedInstrument.numberOfStrings > _uiState.value.pitchesOfTuning.size) {
            while (_uiState.value.pitchesOfTuning.size < selectedInstrument.numberOfStrings) {
                _uiState.value.pitchesOfTuning.add(
                    concert_pitch
                )
            }
        }
        else if (selectedInstrument.numberOfStrings < _uiState.value.pitchesOfTuning.size) {
            while (_uiState.value.pitchesOfTuning.size > selectedInstrument.numberOfStrings) {
                _uiState.value.pitchesOfTuning.removeLast()
            }
        }
    }

    fun updatePitchInList(rowPosition: Int, pos: Int) {
        if (rowPosition >= 0 && rowPosition < _uiState.value.pitchesOfTuning.size) {
            _uiState.value.pitchesOfTuning = _uiState.value.pitchesOfTuning.also { it[rowPosition] = pitchList[pos]}
        }
    }

    fun insertTuning(tuningName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (tuningName.isNotBlank()) {
                userSettingsRepository.insertTuning(
                    Tuning(
                        tuningId = 0,
                        name = tuningName,
                        instrumentId = _uiState.value.selectedInstrument!!.instrumentId
                    )
                )
                val tuningId = userSettingsRepository.getLastTuningId()
                for (pitch in _uiState.value.pitchesOfTuning) {
                    userSettingsRepository.insertPitchTuningCrossRef(
                        pitchTuningCrossRef = PitchTuningCrossRef(
                            tuningId,
                            pitch.frequency
                        )
                    )
                }
            }
        }
    }

}