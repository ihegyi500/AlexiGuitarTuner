package com.example.alexiguitartuner.feat_metronome.data

import android.media.AudioManager
import android.media.ToneGenerator
import com.example.alexiguitartuner.feat_metronome.domain.Beat
import com.example.alexiguitartuner.feat_metronome.domain.MetronomeState
import com.example.alexiguitartuner.feat_metronome.domain.MetronomeState.Companion.initial_state
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis


class MetronomeRepository {
    
    companion object {
        private const val TONE_LENGTH = 50
        const val MIN_NOTE = 3
        private const val MAX_NOTE = 8
        private const val MIN_IN_MILLIS = 60000
    }

    private var toneGenerator : ToneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    private var metronomePlayerJob : Job? = null

    private var _metronomeState = MutableStateFlow(initial_state)
    val metronomeState : StateFlow<MetronomeState> = _metronomeState.asStateFlow()

    fun setRhythm() {
        _metronomeState.update {
            it.copy(
                rhythm = it.rhythm.getNextRhythm()
            )
        }
        setBPM(_metronomeState.value.bpm)
    }

    fun setBPM(value : Int) {
        _metronomeState.update {
            it.copy(
                bpm = value,
                tempo = (MIN_IN_MILLIS / (it.bpm * it.rhythm.value)).toLong()
            )
        }
    }

    suspend fun playMetronome() {
        if (!_metronomeState.value.isPlaying) {
                _metronomeState.update { it.copy(isPlaying = true) }
                metronomePlayerJob = CoroutineScope(Dispatchers.Default).launch {
                    while (_metronomeState.value.isPlaying) {
                        val time = measureTimeMillis {
                            toneGenerator.startTone(
                                _metronomeState.value.beatList[_metronomeState.value.beatListIterator].value,
                                TONE_LENGTH
                            )
                            toneGenerator.stopTone()

                            if (_metronomeState.value.beatListIterator == (_metronomeState.value.beatList.size - 1))
                                _metronomeState.update { it.copy(beatListIterator = 0) }
                            else
                                _metronomeState.update { it.copy(beatListIterator = _metronomeState.value.beatListIterator + 1) }
                        }
                        delay(_metronomeState.value.tempo - time - TONE_LENGTH)
                    }
                }
            }
    }

    fun pauseMetronome(){
        if (_metronomeState.value.isPlaying) {
            if (metronomePlayerJob != null) {
                metronomePlayerJob?.cancel()
            }
            _metronomeState.update { it.copy(isPlaying = false) }
            _metronomeState.update { it.copy(beatListIterator = 0) }
        }
    }

    fun insertNote() {
        if (_metronomeState.value.beatList.size < MAX_NOTE) {
            _metronomeState.update {
                it.copy(
                    beatList = it.beatList
                        .apply {
                            add(Beat.LOUD)
                        }
                )
            }
        } else {
            throw Exception("Only $MAX_NOTE notes are allowed!")
        }
    }

    fun removeNote() {
        if (_metronomeState.value.beatList.size > MIN_NOTE ) {
            _metronomeState.update {
                it.copy(
                    beatList = it.beatList
                        .apply {
                            if (_metronomeState.value.beatListIterator != this.size - 1) removeLast()
                        }
                )
            }
        } else {
            throw Exception("At least $MIN_NOTE notes are necessary!")
        }
    }

    fun setToneByIndex(index : Int) {
        _metronomeState.update {
            it.copy(
                beatList = _metronomeState.value.beatList.apply {
                    this[index] = _metronomeState.value.beatList[index].getNextTone()
                }
            )
        }
    }
}