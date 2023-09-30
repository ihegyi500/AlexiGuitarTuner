package com.example.alexiguitartuner.feat_tuner.data.fakes

import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_tuner.domain.IPitchDetectionRepository

class FakePitchDetectionRepository : IPitchDetectionRepository {
    companion object {
        private const val PITCH_DIFF = 0.4F
        private const val FAULT_DIFF = 0.2F
    }

    var pitchList = mutableListOf<Pitch>()
    override fun getPitchName(frequency: Double): String {
        var res = ""
            for (pitch in pitchList) {
                if (frequency > pitch.frequency - PITCH_DIFF
                    && frequency < pitch.frequency + PITCH_DIFF
                ) {
                    res = when {
                        frequency < pitch.frequency - FAULT_DIFF -> "Below " + pitch.name
                        frequency > pitch.frequency + FAULT_DIFF -> "Above " + pitch.name
                        else -> pitch.name
                    }
                }
            }
        return res
    }
}