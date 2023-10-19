package com.ihegyi.alexiguitartuner.feat_tuner.data.fakes

import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import com.ihegyi.alexiguitartuner.commons.domain.entities.PitchTuningCrossRef
import com.ihegyi.alexiguitartuner.commons.domain.entities.Tuning
import com.ihegyi.alexiguitartuner.commons.domain.entities.UserSettings
import com.ihegyi.alexiguitartuner.feat_tuner.domain.ButtonGenerationRepository

class FakeButtonGenerationRepository : ButtonGenerationRepository {

    private val userSettings = UserSettings(1,1,useSharp = true,useEnglish = true)
    private var pitches = mutableListOf<Pitch>()
    private var pitchTuningCrossRef = mutableListOf<PitchTuningCrossRef>()
    private var tunings = mutableListOf<Tuning>()

    init {
        ('A'..'H').forEachIndexed { index, c ->
            pitches.add(
                Pitch(index.toDouble(), c.toString() + index)
            )
            if (index < 4) {
                pitchTuningCrossRef.add(
                    PitchTuningCrossRef(
                        userSettings.tuningId, index.toDouble()
                    )
                )
            }
            tunings.add(
                index,
                Tuning(
                    index.toLong() + 1,
                    "Tuning $c",
                    index.toLong()
                )
            )
        }
    }

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