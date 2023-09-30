package com.example.alexiguitartuner.feat_tuner.data

import com.example.alexiguitartuner.commons.data.db.AppDatabase
import com.example.alexiguitartuner.commons.domain.entities.Pitch
import com.example.alexiguitartuner.feat_tuner.domain.IButtonGenerationRepository
import javax.inject.Inject

class ButtonGenerationRepository @Inject constructor(
    private val database: AppDatabase
) : IButtonGenerationRepository {
    override suspend fun getPitchesOfLastTuning(): List<Pitch> = database.pitchDAO.getPitchesByLastTuning()
    override suspend fun getTuningNameBySettings() = database.tuningDAO.getTuningNameBySettings()
}