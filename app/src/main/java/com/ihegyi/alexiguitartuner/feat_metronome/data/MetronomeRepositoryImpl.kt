package com.ihegyi.alexiguitartuner.feat_metronome.data

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import com.ihegyi.alexiguitartuner.feat_metronome.domain.Beat
import com.ihegyi.alexiguitartuner.feat_metronome.domain.MetronomeRepository
import com.ihegyi.alexiguitartuner.feat_metronome.domain.MetronomeState
import com.ihegyi.alexiguitartuner.feat_metronome.domain.MetronomeState.Companion.initial_state
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


class MetronomeRepositoryImpl : MetronomeRepository {
    
    companion object {
        private const val TONE_LENGTH = 50
        const val MIN_NOTE = 3
        private const val MAX_NOTE = 8
        private const val MIN_IN_MILLIS = 60000
    }

    private var toneGenerator : ToneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    private var metronomePlayerJob : Job? = null

    private var _metronomeState = MutableStateFlow(initial_state)
    override val metronomeState : StateFlow<MetronomeState> = _metronomeState.asStateFlow()

    override fun setRhythm() {
        _metronomeState.update {
            it.copy(
                rhythm = it.rhythm.getNextRhythm()
            )
        }
        setBPM(_metronomeState.value.bpm)
    }

    override fun setBPM(value : Int) {
        _metronomeState.update {
            it.copy(
                bpm = value,
                tempo = (MIN_IN_MILLIS / (value * it.rhythm.value)).toLong()
            )
        }
    }

    override suspend fun playMetronome() {
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
                            Log.d("Metronome", "${_metronomeState.value.beatList[_metronomeState.value.beatListIterator].value} played!")

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

    override fun pauseMetronome(){
        if (_metronomeState.value.isPlaying) {
            if (metronomePlayerJob != null) {
                metronomePlayerJob?.cancel()
            }
            _metronomeState.update {
                it.copy(
                    isPlaying = false,
                    beatListIterator = 0
                )
            }
        }
    }

    override fun insertNote() {
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

    override fun removeNote() {
        if (_metronomeState.value.beatList.size > MIN_NOTE ) {
            _metronomeState.update {
                it.copy(
                    beatList = it.beatList
                        .apply {
                            if (it.beatListIterator != this.size - 1) removeAt(size - 1)
                        }
                )
            }
        } else {
            throw Exception("At least $MIN_NOTE notes are necessary!")
        }
    }

    override fun setToneByIndex(index : Int) {
        _metronomeState.update {
            it.copy(
                beatList = it.beatList.apply {
                    this[index] = it.beatList[index].getNextTone()
                }
            )
        }
    }
}