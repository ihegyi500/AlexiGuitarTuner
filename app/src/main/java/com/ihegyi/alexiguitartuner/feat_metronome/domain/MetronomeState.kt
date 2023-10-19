package com.ihegyi.alexiguitartuner.feat_metronome.domain

data class MetronomeState(
    val isPlaying: Boolean,
    val bpm: Int,
    val tempo: Long,
    val rhythm : Rhythm,
    val beatList: MutableList<Beat>,
    val beatListIterator: Int
) {
    companion object {
        val initial_state = MetronomeState(
            isPlaying = false,
            bpm = 100,
            tempo = 600,
            rhythm = Rhythm.QUARTER,
            beatList = MutableList(4) {Beat.LOUD},
            beatListIterator = 0
        )
    }
}
