package com.ihegyi.alexiguitartuner.feat_tuner.data.fakes

import com.ihegyi.alexiguitartuner.feat_tuner.domain.PitchGenerationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class FakePitchGenerationRepository : PitchGenerationRepository {

    private val coroutineScope = CoroutineScope(UnconfinedTestDispatcher())
    var jobState = false
    override fun initAudioTrack() {
    }

    override fun generateAudioTrack(frequency: Double) = coroutineScope.launch{
        jobState = coroutineScope.coroutineContext.job.isActive
    }

    override fun startPitchGeneration(frequency: Double) {
        generateAudioTrack(frequency)
    }

    override fun stopPitchGeneration() {
        coroutineScope.cancel()
        jobState = coroutineScope.coroutineContext.job.isCancelled
    }

}