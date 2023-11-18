package com.ihegyi.alexiguitartuner.commons.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihegyi.alexiguitartuner.commons.data.UserSettingsRepositoryImpl
import com.ihegyi.alexiguitartuner.commons.domain.UserSettingsRepository
import com.ihegyi.alexiguitartuner.commons.domain.entities.Instrument
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import com.ihegyi.alexiguitartuner.commons.domain.entities.UserSettings
import com.ihegyi.alexiguitartuner.commons.presentation.state.SelectTuningUIState
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
class SelectTuningViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectTuningUIState.initial_state)
    val uiState: StateFlow<SelectTuningUIState> = _uiState.asStateFlow()

    private val _userSettings = MutableStateFlow(UserSettingsRepositoryImpl.initial_user_setting)
    val userSettings: StateFlow<UserSettings> = _userSettings.asStateFlow()

    suspend fun initTuningList() {
        viewModelScope.launch(dispatcher) {
            userSettingsRepository.getUserSettings().collect{
                _userSettings.value = it
                val selectedTuningId = userSettings.value.tuningId
                userSettingsRepository.getInstruments().collectLatest { instruments ->
                    val selectedInstrument = userSettingsRepository.getInstrumentByTuning(selectedTuningId)
                    val tuningList = userSettingsRepository.getTuningsByInstrument(selectedInstrument.instrumentId).firstOrNull() ?: emptyList()
                    val selectedTuning = userSettingsRepository.getTuningById(selectedTuningId)
                    _uiState.update {
                        _uiState.value.copy(
                            instrumentList = instruments,
                            selectedInstrument = selectedInstrument,
                            tuningList = tuningList,
                            selectedTuning = selectedTuning
                        )
                    }
                }
            }
        }
    }

    suspend fun selectInstrument(instrument: Instrument?) {
        val tuningList = userSettingsRepository.getTuningsByInstrument(instrument?.instrumentId ?: 0).firstOrNull() ?: emptyList()
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
        _uiState.update {
            _uiState.value.copy(
                selectedTuning = tuning
            )
        }
    }

    fun updateSelectedTuning() {
        viewModelScope.launch(dispatcher) {
            val userSettings = userSettings.value.copy(
                tuningId = _uiState.value.selectedTuning!!.tuningId
            )
            userSettingsRepository.updateUserSettings(userSettings)
        }
    }



}