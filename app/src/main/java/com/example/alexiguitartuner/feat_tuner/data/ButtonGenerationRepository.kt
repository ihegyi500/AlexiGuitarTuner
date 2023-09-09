package com.example.alexiguitartuner.feat_tuner.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import javax.inject.Inject

class ButtonGenerationRepository @Inject constructor(
    private val database: AppDatabase
) {
    suspend fun getPitchesOfLastTuning(): List<Pitch> = database.pitchDAO.getPitchesByLastTuning()
    suspend fun getTuningNameBySettings() = database.tuningDAO.getTuningNameBySettings()
}