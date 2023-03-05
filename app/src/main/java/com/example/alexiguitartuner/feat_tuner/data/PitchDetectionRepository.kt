package com.example.alexiguitartuner.feat_tuner.data

import android.util.Log
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory.fromDefaultMicrophone
import be.tarsos.dsp.pitch.PitchProcessor
import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.data.db.ChordDAO
import com.example.alexiguitartuner.feat_tuner.domain.AudioProcessingThread
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.math.roundToInt


class PitchDetectionRepository @Inject constructor(
    private val chordDAO: ChordDAO
) {

    private var audioProcessThread: AudioProcessingThread? = null
    private var audioDispatcher: AudioDispatcher? = null

    private var _detectedHz = MutableStateFlow(0.0)
    val detectedHz : StateFlow<Double> = _detectedHz.asStateFlow()

    @OptIn(DelicateCoroutinesApi::class)
    private val pitchProcessor = PitchProcessor(
        PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
        44100F,
        4096
    ) { pitchDetectionResult, _ ->
        val pitch = pitchDetectionResult.pitch.toDouble()
        GlobalScope.launch(Dispatchers.Default) {
            _detectedHz.value = (pitch * 100.0).roundToInt() / 100.0
            //Log.d("HERTZ","Hertz is: ${_detectedHz.value}")
        }
    }

    fun startAudioProcessing() {
        if (audioProcessThread == null) {
            audioDispatcher = fromDefaultMicrophone(44100, 4096, 3072)
            audioDispatcher?.addAudioProcessor(pitchProcessor)
            audioProcessThread = AudioProcessingThread(audioDispatcher)
            audioProcessThread?.start()
            Log.d("HERTZ",
                "Thread is running: ${audioProcessThread?.isRunning}, , ${audioDispatcher?.isStopped}")
            GlobalScope.launch(Dispatchers.IO) {
                chordDAO.getChordWithChordTables()
            }
        }
    }

    fun stopAudioProcessing() {
        if (audioProcessThread?.isRunning == true) {
            audioProcessThread?.interrupt()
            audioProcessThread?.exit()
            audioProcessThread = null
            audioDispatcher?.stop()
            audioDispatcher?.removeAudioProcessor(pitchProcessor)
            audioDispatcher = null
            Log.d("HERTZ",
                "Thread is stopped: ${audioProcessThread?.isRunning} , ${audioDispatcher?.isStopped}")
        }
    }
}
