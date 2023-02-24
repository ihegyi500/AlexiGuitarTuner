package com.example.alexiguitartuner.feat_tuner.domain

import com.example.alexiguitartuner.commons.domain.Pitch
import com.example.alexiguitartuner.feat_tuner.data.TunerDataSource
import javax.inject.Inject


class FindPitchUseCase @Inject constructor(
    private val tunerDataSource: TunerDataSource
) {
    operator fun invoke(frequency : Double) : String  {
        if(tunerDataSource.pitchList.isEmpty())
            tunerDataSource.initializePitchList()
        var res = "-"
        for (pitch : Pitch in tunerDataSource.pitchList) {
            if(frequency > pitch.frequency - TunerDataSource.PITCH_DIFF
                && frequency < pitch.frequency + TunerDataSource.PITCH_DIFF
            ) {
                res = when {
                    frequency < pitch.frequency - TunerDataSource.FAULT_DIFF -> "Below " + pitch.name
                    frequency > pitch.frequency + TunerDataSource.FAULT_DIFF -> "Above " + pitch.name
                    else -> pitch.name
                }
            }
        }
        return res
    }

}