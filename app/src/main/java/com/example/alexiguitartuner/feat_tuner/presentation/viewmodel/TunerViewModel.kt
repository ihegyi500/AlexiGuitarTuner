package com.example.alexiguitartuner.feat_tuner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_tuner.domain.ButtonGenerationRepository
import com.example.alexiguitartuner.feat_tuner.domain.PitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.domain.PitchGenerationRepository
import com.example.alexiguitartuner.feat_tuner.presentation.state.PitchButtonsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TunerViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val pitchGenerationRepository: PitchGenerationRepository,
    private val pitchDetectionRepository: PitchDetectionRepository,
    private val buttonGenerationRepository: ButtonGenerationRepository
) : ViewModel() {

    val detectedPitch = pitchDetectionRepository.detectedPitch
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Pitch( 0.0,"")
        )

    private var _pitchButtonsUIState = MutableStateFlow(PitchButtonsUIState.initial_state)
    val pitchButtonsUIState : StateFlow<PitchButtonsUIState> = _pitchButtonsUIState.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            pitchDetectionRepository.initPitchList()
        }
    }

    suspend fun initPitchButtons() {
        viewModelScope.launch(dispatcher) {
            _pitchButtonsUIState.update {
                _pitchButtonsUIState.value.copy(
                    tuningName = buttonGenerationRepository.getTuningNameBySettings(),
                    pitchList = buttonGenerationRepository.getPitchesOfLastTuning()
                )
            }
            /*
            _pitchButtonsUIState.value = _pitchButtonsUIState.value.copy(
                tuningName = buttonGenerationRepository.getTuningNameBySettings(),
                pitchList = buttonGenerationRepository.getPitchesOfLastTuning()
            )*/
        }
    }

    fun startAudioProcessing() {
        pitchDetectionRepository.startAudioProcessing()
    }

    fun stopAudioProcessing() {
        pitchDetectionRepository.stopAudioProcessing()
    }

    fun startPitchGeneration(frequency:Double) {
        pitchGenerationRepository.startPitchGeneration(frequency)
    }

    fun stopPitchGeneration() {
        pitchGenerationRepository.stopPitchGeneration()
    }

}