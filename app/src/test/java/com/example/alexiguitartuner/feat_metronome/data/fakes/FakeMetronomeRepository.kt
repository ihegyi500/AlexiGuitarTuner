package com.example.alexiguitartuner.feat_metronome.data.fakes

import com.example.alexiguitartuner.feat_metronome.data.MetronomeRepository
import com.example.alexiguitartuner.feat_metronome.domain.Beat
import com.example.alexiguitartuner.feat_metronome.domain.IMetronomeRepository
import com.example.alexiguitartuner.feat_metronome.domain.MetronomeState
import kotlinx.coroutines.flow.update

class FakeMetronomeRepository : IMetronomeRepository {
    companion object {
        private const val TONE_LENGTH = 50
        const val MIN_NOTE = 3
        private const val MAX_NOTE = 8
        private const val MIN_IN_MILLIS = 60000
    }

    lateinit var metronomeState : MetronomeState

    override fun setRhythm() {
        metronomeState = metronomeState.copy(
            rhythm = metronomeState.rhythm.getNextRhythm()
        )
    }

    override fun setBPM(value: Int) {
        metronomeState = metronomeState.copy(
            bpm = value
        )
    }

    override fun insertNote() {
        if (metronomeState.beatList.size < MAX_NOTE) {
            metronomeState = metronomeState.copy(
                beatList = metronomeState.beatList
                    .apply {
                        add(Beat.LOUD)
                    }
            )
        } else {
            throw Exception("Only $MAX_NOTE notes are allowed!")
        }
    }

    override fun removeNote() {
        TODO("Not yet implemented")
    }

    override fun setToneByIndex(index: Int) {
        TODO("Not yet implemented")
    }
}