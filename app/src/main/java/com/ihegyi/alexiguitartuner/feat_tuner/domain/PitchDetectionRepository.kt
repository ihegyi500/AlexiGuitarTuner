package com.ihegyi.alexiguitartuner.feat_tuner.domain

import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

interface PitchDetectionRepository {

    val detectedPitch: StateFlow<Pitch>
    fun getPitchName(frequency: Double): String
    suspend fun initPitchList()
    fun startAudioProcessing() : Job
    fun stopAudioProcessing()
}