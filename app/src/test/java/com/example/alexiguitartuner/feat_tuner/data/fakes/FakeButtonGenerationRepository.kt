package com.example.alexiguitartuner.feat_tuner.data.fakes

import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.example.alexiguitartuner.commons.domain.entities.Tuning
import com.example.alexiguitartuner.commons.domain.entities.UserSettings
import com.example.alexiguitartuner.feat_tuner.domain.IButtonGenerationRepository

class FakeButtonGenerationRepository : IButtonGenerationRepository {

    val userSettings = UserSettings(1,1,useSharp = true,useEnglish = true)
    var pitches = mutableListOf<Pitch>()
    var pitchTuningCrossRef = mutableListOf<PitchTuningCrossRef>()
    var tunings = mutableListOf<Tuning>()

    override suspend fun getPitchesOfLastTuning(): List<Pitch> {
        return mutableListOf<Pitch>().apply {
            pitchTuningCrossRef.filter {
                it.tuningId == userSettings.tuningId
            }.forEach { crossRef ->
                add(
                    pitches.find { pitch ->
                        crossRef.frequency == pitch.frequency
                    }!!
                )
            }
        }
    }

    override suspend fun getTuningNameBySettings(): String {
        return tunings.find {
            userSettings.tuningId == it.tuningId
        }!!.name
    }
}