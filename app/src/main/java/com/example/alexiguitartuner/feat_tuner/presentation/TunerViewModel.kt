package com.example.alexiguitartuner.feat_tuner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.commons.domain.Pitch
import com.example.alexiguitartuner.feat_tuner.data.ButtonGenerationRepository
import com.example.alexiguitartuner.feat_tuner.data.PitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.data.PitchGenerationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TunerViewModel @Inject constructor(
    private val pitchGenerationRepository: PitchGenerationRepository,
    private val pitchDetectionRepository: PitchDetectionRepository,
    private val buttonGenerationRepository: ButtonGenerationRepository,
    ) : ViewModel() {

    val detectedPitch = pitchDetectionRepository.detectedPitch
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Pitch("", 0.0)
        )

    init {
        viewModelScope.launch {
            pitchDetectionRepository.initPitchList()
        }
    }

    fun startAudioProcessing() {
        viewModelScope.launch {
            pitchDetectionRepository.startAudioProcessing()
        }
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

    fun getPitchesOfLastTuning() : Flow<List<Pitch>> = buttonGenerationRepository.getPitchesOfLastTuning()

}