package com.example.alexiguitartuner.feat_tuner.data.fakes

import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_tuner.domain.PitchDetectionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class FakePitchDetectionRepository : PitchDetectionRepository {
    companion object {
        private const val PITCH_DIFF = 0.4F
        private const val FAULT_DIFF = 0.2F
    }

    var pitchList = mutableListOf<Pitch>()
    override val detectedPitch: StateFlow<Pitch> = MutableStateFlow(Pitch(440.0,"A4"))
    private val coroutineScope = CoroutineScope(UnconfinedTestDispatcher())
    var jobState = false

    override fun getPitchName(frequency: Double): String {
        var res = ""
            for (pitch in pitchList) {
                if (frequency > pitch.frequency - PITCH_DIFF
                    && frequency < pitch.frequency + PITCH_DIFF
                ) {
                    res = when {
                        frequency < pitch.frequency - FAULT_DIFF -> "Below " + pitch.name
                        frequency > pitch.frequency + FAULT_DIFF -> "Above " + pitch.name
                        else -> pitch.name
                    }
                }
            }
        return res
    }

    override suspend fun initPitchList() {
        pitchList = emptyList<Pitch>().toMutableList().apply {
            ('A'..'Z').forEachIndexed { index, c ->
                add(
                    Pitch(
                        index + 1.toDouble(),
                        c.toString() + index
                    )
                )
            }
        }
    }

    override fun startAudioProcessing() = coroutineScope.launch {
        jobState = coroutineScope.coroutineContext.job.isActive
    }

    override fun stopAudioProcessing() {
        coroutineScope.cancel()
        jobState = coroutineScope.coroutineContext.job.isCancelled
    }
}