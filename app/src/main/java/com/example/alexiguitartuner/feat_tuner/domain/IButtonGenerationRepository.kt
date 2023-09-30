package com.example.alexiguitartuner.feat_tuner.domain

import com.example.alexiguitartuner.commons.domain.entities.Pitch

interface IButtonGenerationRepository {
    suspend fun getPitchesOfLastTuning(): List<Pitch>
    suspend fun getTuningNameBySettings() : String
}