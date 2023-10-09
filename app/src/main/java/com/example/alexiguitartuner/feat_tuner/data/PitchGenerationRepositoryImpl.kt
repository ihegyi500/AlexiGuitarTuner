package com.example.alexiguitartuner.feat_tuner.data

import android.media.*
import com.example.alexiguitartuner.feat_tuner.domain.PitchGenerationRepository
import kotlinx.coroutines.*
import kotlin.math.sin

class PitchGenerationRepositoryImpl() : PitchGenerationRepository {

    companion object {
        const val SAMPLE_RATE = 44100
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_STEREO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        const val AMPLITUDE = 32767
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var audioTrack : AudioTrack? = null
    private var currentFrequency = 0.0
    private var previousFrequency = 0.0
    private var isPlaying: Boolean = false

    private val buffLength: Int = AudioTrack.getMinBufferSize(
        SAMPLE_RATE,
        CHANNEL_CONFIG,
        AUDIO_FORMAT
    )

    override fun initAudioTrack() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setSampleRate(SAMPLE_RATE)
            .setEncoding(AUDIO_FORMAT)
            .setChannelMask(CHANNEL_CONFIG)
            .build()

        audioTrack = AudioTrack(
            audioAttributes,
            audioFormat,
            buffLength,
            AudioTrack.MODE_STREAM,
            0
        )
    }

    override fun generateAudioTrack(frequency: Double) = coroutineScope.launch {
        val frame = ShortArray(buffLength)
        val twoPi: Double = 2.0 * Math.PI
        var phase = 0.0

        while (isPlaying) {
            for (i in 0 until buffLength) {
                frame[i] = (AMPLITUDE * sin(phase)).toInt().toShort()
                phase += twoPi * frequency / SAMPLE_RATE
                if (phase > twoPi) {
                    phase -= twoPi
                }
            }
            audioTrack?.write(frame, 0, buffLength)
            delay(1)
        }
    }

    override fun startPitchGeneration(frequency: Double) {
        previousFrequency = currentFrequency
        if(!isPlaying || frequency != previousFrequency) {
            if (frequency != previousFrequency) {
                stopPitchGeneration()
                currentFrequency = frequency
            }
            isPlaying = true
            initAudioTrack()
            audioTrack?.play()
            generateAudioTrack(currentFrequency)
        }
        else {
            stopPitchGeneration()
        }
    }

    override fun stopPitchGeneration() {
        isPlaying = false
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
        coroutineScope.coroutineContext.cancelChildren()
    }
}