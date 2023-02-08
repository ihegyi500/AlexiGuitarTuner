package com.example.alexiguitartuner.feat_tuner.data

import android.util.Log
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory.fromDefaultMicrophone
import be.tarsos.dsp.pitch.PitchProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PitchDetectionRepository {

    private var audioProcessThread: AudioProcessingThread? = null
    private var audioDispatcher: AudioDispatcher? = null

    private val _detectedHz = MutableStateFlow(0.0)
    val detectedHz : StateFlow<Double> = _detectedHz.asStateFlow()

    private val pitchProcessor = PitchProcessor(
        PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
        22050f,
        1024
    ) { pitchDetectionResult, audioEvent ->
        _detectedHz.value = pitchDetectionResult.pitch.toDouble()//String.format("%.2f", pitchDetectionResult.pitch.toDouble()).toDouble()
        Log.d("HERTZ","Hertz is: ${String.format("%.2f", pitchDetectionResult.pitch.toDouble())}")
    }

    fun startAudioProcessing() {
        audioDispatcher = fromDefaultMicrophone(22050, 1024, 0)
        audioDispatcher?.addAudioProcessor(pitchProcessor)
        audioProcessThread = AudioProcessingThread(audioDispatcher)
        audioProcessThread?.start()
        Log.d("HERTZ","Thread is running: ${audioProcessThread?.isRunning}, , ${audioDispatcher?.isStopped}")
    }

    fun stopAudioProcessing() {
        audioProcessThread?.exit()
        audioProcessThread = null
        audioDispatcher?.stop()
        audioDispatcher?.removeAudioProcessor(pitchProcessor)
        audioDispatcher = null
        Log.d("HERTZ","Thread is running: ${audioProcessThread?.isRunning} , ${audioDispatcher?.isStopped}")
    }
}