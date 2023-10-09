package com.example.alexiguitartuner.feat_tuner.data.fakes

import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_tuner.domain.PitchDetectionRepository
import com.example.alexiguitartuner.feat_tuner.domain.PitchGenerationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

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