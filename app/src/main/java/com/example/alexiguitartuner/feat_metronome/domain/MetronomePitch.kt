package com.example.alexiguitartuner.feat_metronome.domain

data class MetronomePitch(
    var tone : Tone,
    var isCurrent : Boolean
) {
    fun setIsCurrent(isCurrent: Boolean) {
        this.isCurrent = isCurrent
    }
    fun getNextTone() {
        tone = tone.getNextTone()
    }
}
