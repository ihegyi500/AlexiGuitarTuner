package com.example.alexiguitartuner.feat_tuner.data

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory.fromDefaultMicrophone
import be.tarsos.dsp.pitch.PitchProcessor
import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_tuner.domain.PitchDetectionRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.math.roundToInt

class PitchDetectionRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : PitchDetectionRepository {
    companion object {
        const val SAMPLE_RATE = 44100
        const val BUFFER_SIZE = 8192
        const val OVERLAP = 3072
        const val PITCH_DIFF = 0.4F
        const val FAULT_DIFF = 0.2F
    }

    private var audioDispatcher: AudioDispatcher? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private var pitchList = mutableListOf<Pitch>()

    private val _detectedPitch = MutableStateFlow(Pitch(440.0,"A4"))
    override val detectedPitch: StateFlow<Pitch> = _detectedPitch

    private val pitchProcessor = PitchProcessor(
        PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
        SAMPLE_RATE.toFloat(),
        BUFFER_SIZE
    ) { pitchDetectionResult, _ ->
        val pitchFrequency = (pitchDetectionResult.pitch * 100.0).roundToInt() / 100.0
        val pitchName = getPitchName(pitchFrequency)
        _detectedPitch.update {
            Pitch(pitchFrequency,pitchName)
        }
    }

    override suspend fun initPitchList() {
        pitchList = database.pitchDAO.getPitches().toMutableList()
    }

    override fun startAudioProcessing() = coroutineScope.launch {
        if (audioDispatcher == null) {
            audioDispatcher = fromDefaultMicrophone(SAMPLE_RATE, BUFFER_SIZE, OVERLAP)
            audioDispatcher?.addAudioProcessor(pitchProcessor)
            audioDispatcher?.run()
        }
    }

    override fun stopAudioProcessing() {
        if (audioDispatcher != null) {
            audioDispatcher?.stop()
            audioDispatcher?.removeAudioProcessor(pitchProcessor)
            audioDispatcher = null
            coroutineScope.coroutineContext.cancelChildren()
        }
    }

    override fun getPitchName(frequency: Double): String {
        var res = ""
        for (pitch in pitchList) {
            if(frequency > pitch.frequency - PITCH_DIFF
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

}