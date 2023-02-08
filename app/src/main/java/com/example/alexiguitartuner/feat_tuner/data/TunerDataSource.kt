package com.example.alexiguitartuner.feat_tuner.data

import com.example.alexiguitartuner.feat_tuner.domain.Pitch

object TunerDataSource {

    const val MAX_HZ = 16.0F
    const val PITCH_DIFF = 0.4F
    const val FAULT_DIFF = 0.05F

    val pitchList : List<Pitch> = listOf(
        Pitch("C",8.175799),
        Pitch("C#",8.661957),
        Pitch("D",9.177024),
        Pitch("D#",9.722718),
        Pitch("E",10.300861),
        Pitch("F",10.913382),
        Pitch("F#",11.562326),
        Pitch("G",12.249857),
        Pitch("G#",12.978272),
        Pitch("A",13.750000),
        Pitch("A#",14.567618),
        Pitch("B",15.433853)
    )

}