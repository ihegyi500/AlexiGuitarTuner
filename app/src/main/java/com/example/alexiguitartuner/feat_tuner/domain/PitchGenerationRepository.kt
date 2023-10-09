package com.example.alexiguitartuner.feat_tuner.domain

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_tuner.data.PitchGenerationRepositoryImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.sin

interface PitchGenerationRepository {

    fun initAudioTrack()
    fun generateAudioTrack(frequency: Double) : Job
    fun startPitchGeneration(frequency: Double)
    fun stopPitchGeneration()
}