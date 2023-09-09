package com.example.alexiguitartuner.commons.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.commons.data.UserSettingsRepository
import com.example.alexiguitartuner.commons.domain.TuningSelectorViewModel
import com.example.alexiguitartuner.commons.domain.entities.Instrument
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import com.example.alexiguitartuner.commons.presentation.state.SelectTuningUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectTuningViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel(), TuningSelectorViewModel {

    private val _uiState = MutableStateFlow(SelectTuningUIState.initial_state)
    override val uiState: StateFlow<SelectTuningUIState> = _uiState.asStateFlow()

    private val _userSettings = MutableStateFlow(UserSettingsRepository.initial_user_setting)
    val userSettings: StateFlow<UserSettings> = _userSettings.asStateFlow()

    suspend fun initTuningList() {
        viewModelScope.launch(Dispatchers.IO) {
            userSettingsRepository.getUserSettings().collect{
                _userSettings.value = it
                val selectedTuningId = userSettings.value.tuningId
                userSettingsRepository.getInstruments().collectLatest { instruments ->
                    val selectedInstrument = userSettingsRepository.getInstrumentByTuning(selectedTuningId)
                    val tuningList = userSettingsRepository.getTuningsByInstrument(selectedInstrument.instrumentId).firstOrNull() ?: emptyList()
                    val selectedTuning = userSettingsRepository.getTuningById(selectedTuningId)
                    _uiState.value = _uiState.value.copy(
                        instrumentList = instruments,
                        selectedInstrument = selectedInstrument,
                        tuningList = tuningList,
                        selectedTuning = selectedTuning
                    )
                }
            }
        }
    }

    override suspend fun selectInstrument(instrument: Instrument?) {
        val tuningList = userSettingsRepository.getTuningsByInstrument(instrument?.instrumentId ?: 0).firstOrNull() ?: emptyList()
        val selectedTuning = tuningList.firstOrNull()
        _uiState.value = _uiState.value.copy(
            selectedInstrument = instrument,
            tuningList = tuningList
        )
        selectTuning(selectedTuning)
    }

    override suspend fun selectTuning(tuning: Tuning?) {
        _uiState.value = _uiState.value.copy(
            selectedTuning = tuning
        )
    }

    fun updateSelectedTuning() {
        viewModelScope.launch(Dispatchers.IO) {
            val userSettings = userSettings.value.copy(
                tuningId = _uiState.value.selectedTuning!!.tuningId
            )
            userSettingsRepository.updateUserSettings(userSettings)
        }
    }



}