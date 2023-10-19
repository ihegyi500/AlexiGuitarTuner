package com.ihegyi.alexiguitartuner.feat_tuner.domain

import kotlinx.coroutines.Job

interface PitchGenerationRepository {

    fun initAudioTrack()
    fun generateAudioTrack(frequency: Double) : Job
    fun startPitchGeneration(frequency: Double)
    fun stopPitchGeneration()
}