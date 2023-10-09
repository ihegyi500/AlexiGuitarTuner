package com.example.alexiguitartuner.feat_metronome.data.fakes

import com.example.alexiguitartuner.feat_metronome.data.MetronomeRepositoryImpl
import com.example.alexiguitartuner.feat_metronome.domain.Beat
import com.example.alexiguitartuner.feat_metronome.domain.MetronomeRepository
import com.example.alexiguitartuner.feat_metronome.domain.MetronomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

class FakeMetronomeRepository : MetronomeRepository {
    companion object {
        const val MIN_NOTE = 3
        const val MAX_NOTE = 8
        private const val MIN_IN_MILLIS = 60000
    }

    private var _metronomeState : MutableStateFlow<MetronomeState> = MutableStateFlow(MetronomeState.initial_state)
    override val metronomeState : StateFlow<MetronomeState> = _metronomeState.asStateFlow()

    fun resetMetronomeState() {

        _metronomeState.updateAndGet {
            MetronomeState.initial_state
        }
    }

    override suspend fun playMetronome() {
        _metronomeState.update {
            it.copy(
                isPlaying = true
            )
        }
    }

    override fun pauseMetronome() {
        _metronomeState.update {
            it.copy(
                isPlaying = false
            )
        }
    }

    override fun setRhythm() {
        _metronomeState.update {
            it.copy(
                rhythm = it.rhythm.getNextRhythm()
            )
        }
        setBPM(_metronomeState.value.bpm)
    }

    override fun setBPM(value: Int) {
        _metronomeState.update {
            it.copy(
                bpm = value,
                tempo = (MIN_IN_MILLIS / (value * it.rhythm.value)).toLong()
            )
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
        if (_metronomeState.value.beatList.size > MIN_NOTE) {
            _metronomeState.update {
                it.copy(
                    beatList = it.beatList
                        .apply {
                            if (it.beatListIterator != this.size - 1) removeLast()
                        }
                )
            }
        } else {
            throw Exception("At least $MIN_NOTE notes are necessary!")
        }
    }

    override fun setToneByIndex(index: Int) {
        _metronomeState.update {
            it.copy(
                beatList = it.beatList.apply {
                    this[index] = it.beatList[index].getNextTone()
                }
            )
        }
    }
}