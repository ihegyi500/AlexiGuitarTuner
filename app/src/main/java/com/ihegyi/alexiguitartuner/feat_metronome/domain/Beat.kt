package com.ihegyi.alexiguitartuner.feat_metronome.domain

import android.media.ToneGenerator

enum class Beat(val value : Int){
    SILENT(100),
    LOUD(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE),
    LOUDER(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);

    fun getNextTone() : Beat = values()[(this.ordinal + 1) % values().size]

}