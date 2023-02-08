package com.example.alexiguitartuner.feat_tuner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alexiguitartuner.feat_tuner.data.PitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.data.PitchGenerationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TunerViewModel() : ViewModel() {

    private val repository = PitchDetectionRepository()
    private val pitchGenerationRepository = PitchGenerationRepository()

    private val _detectedHz = MutableStateFlow(0.0)
    val detectedHz : StateFlow<Double> = _detectedHz.asStateFlow()

    fun startAudioProcessing() {

        repository.startAudioProcessing()
        viewModelScope.launch {
            repository.detectedHz.collectLatest {
                _detectedHz.value = it
            }
        }

    }

    fun stopAudioProcessing() {
        repository.stopAudioProcessing()
    }

    fun startPitchGeneration(frequency:Double) {
        pitchGenerationRepository.setSelectedFrequency(frequency)
        pitchGenerationRepository.startPitchGeneration()
    }

    fun stopPitchGeneration() {
        pitchGenerationRepository.stopPitchGeneration()
    }

}