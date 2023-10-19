package com.ihegyi.alexiguitartuner.feat_tuner.domain

import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch

interface ButtonGenerationRepository {
    suspend fun getPitchesOfLastTuning(): List<Pitch>
    suspend fun getTuningNameBySettings() : String
}