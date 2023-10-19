package com.ihegyi.alexiguitartuner.feat_metronome.domain

enum class Rhythm(val value : Int) {
    QUARTER(1),
    EIGHTH(2),
    SIXTEENTH(4);

    fun getNextRhythm() : Rhythm = values()[(this.ordinal + 1) % values().size]

}