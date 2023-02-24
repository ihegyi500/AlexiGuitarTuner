package com.example.alexiguitartuner.feat_tuner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.feat_tuner.data.PitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.data.PitchGenerationRepository
import com.example.alexiguitartuner.feat_tuner.domain.FindPitchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TunerViewModel @Inject constructor(
    private val findPitchUseCase: FindPitchUseCase,
    private val pitchGenerationRepository: PitchGenerationRepository,
    private val pitchDetectionRepository: PitchDetectionRepository
    ) : ViewModel() {

    private var _detectedHz = MutableStateFlow(0.0)
    val detectedHz : StateFlow<Double> = _detectedHz.asStateFlow()

    private var _detectedPitch = MutableStateFlow("-")
    val detectedPitch : StateFlow<String> = _detectedPitch.asStateFlow()

    init {
        viewModelScope.launch {
            pitchDetectionRepository.detectedHz.collectLatest {
                _detectedHz.value = it
                _detectedPitch.value = findPitchUseCase(it)
            }
        }
    }

    fun startAudioProcessing() {
        pitchDetectionRepository.startAudioProcessing()
    }

    fun stopAudioProcessing() {
        pitchDetectionRepository.stopAudioProcessing()
    }

    fun startPitchGeneration(frequency:Double) {
        pitchGenerationRepository.setSelectedFrequency(frequency)
        pitchGenerationRepository.startPitchGeneration()
    }

    fun stopPitchGeneration() {
        pitchGenerationRepository.stopPitchGeneration()
    }

}