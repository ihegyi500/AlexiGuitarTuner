package com.example.alexiguitartuner.feat_tuner.domain

import com.example.alexiguitartuner.commons.domain.entities.Pitch
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

interface PitchDetectionRepository {

    val detectedPitch: StateFlow<Pitch>
    fun getPitchName(frequency: Double): String
    suspend fun initPitchList()
    fun startAudioProcessing() : Job
    fun stopAudioProcessing()
}