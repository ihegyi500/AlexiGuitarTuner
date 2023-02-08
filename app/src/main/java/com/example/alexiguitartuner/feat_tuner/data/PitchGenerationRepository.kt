package com.example.alexiguitartuner.feat_tuner.data

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.*
import kotlin.math.atan
import kotlin.math.sin


class PitchGenerationRepository {

    companion object {
        const val SAMPLE_RATE = 44100
        const val AMPLITUDE = 32767
    }

    private var audioTrack : AudioTrack? = null

    private var frequency = 0.0

    private var isPlaying: Boolean = false

    private val buffLength: Int = AudioTrack.getMinBufferSize(
        SAMPLE_RATE,
        AudioFormat.CHANNEL_OUT_STEREO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private fun initAudioTrack() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setSampleRate(SAMPLE_RATE)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
            .build()

        audioTrack = AudioTrack(
            audioAttributes,
            audioFormat,
            buffLength,
            AudioTrack.MODE_STREAM,
            0
        )
    }

    private fun generateAudioTrack(frequency: Double) {
        val frame = ShortArray(buffLength)
        val twoPi: Double = 8.0 * atan(1.0)
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
        }
    }

    fun startPitchGeneration() {
        if(isPlaying)
            stopPitchGeneration()
        GlobalScope.launch(Dispatchers.IO) {
            isPlaying = true
            initAudioTrack()
            audioTrack?.play()
            generateAudioTrack(frequency)
            Log.d("PITCH","AudioTrack is started: ${audioTrack?.state}")
        }
    }

    fun stopPitchGeneration() {
        if(isPlaying){
            isPlaying = false
            audioTrack?.stop()
            audioTrack?.release()
            Log.d("PITCH","AudioTrack is stopped: ${audioTrack?.state}")
        }
    }

    fun setSelectedFrequency(frequency: Double) {
        this.frequency = frequency
    }

}